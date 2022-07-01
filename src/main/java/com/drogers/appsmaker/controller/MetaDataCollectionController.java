package com.drogers.appsmaker.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.drogers.appsmaker.dao.DatabaseUtils;
import com.drogers.appsmaker.dao.MetaDataCollectionDAO;
import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.factory.GeneratedAppsZipFactory;
import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;
import com.drogers.appsmaker.model.domain.GenerationOptions;
import com.drogers.appsmaker.model.domain.TableMetaData;

@CrossOrigin(origins="*")
@Controller
public class MetaDataCollectionController {

	private static final String SESSION_ATTRIB_DB_CONN_INFO = "dbConnectInfo";
	private static final String SESSION_ATTRIB_GEN_OPTIONS_INFO = "genOptionsInfo";

	private Logger logger = LoggerFactory.getLogger(MetaDataCollectionController.class);
	
	@Value("${apps.gen.work.dir}")
	private String appsGenWorkDir;	
	
	@Value("${apps.gen.zip.file.name}")
	private String appsGenZipFileName;	
	
	@Autowired
	private MetaDataCollectionDAO metaDataCollectionDAO;
	
	@Autowired
	private GeneratedAppsZipFactory appsZipFactory;

	@Autowired
	private DatabaseUtils databaseUtils;
	
	@RequestMapping(value = "/getschemafile", method = RequestMethod.GET)
	public ModelAndView getSchemaFile() {
		return new ModelAndView("uploadSchema");
	}	
	
	@RequestMapping(value = "/getdbconnectinfo", method = RequestMethod.GET)
	public ModelAndView getDatabaseConnectionInfo() {
		return new ModelAndView("dbConnectInfo");
	}
	
	@RequestMapping(value = "/uploadschema", method = RequestMethod.POST)
	public ModelAndView uploadSchemaScript(@RequestParam("dbmsName") String dbmsName,
										   @RequestParam("fileToUpload") MultipartFile uploadedFile,
										   HttpServletRequest request, HttpSession session)
												   throws ScriptException, SQLException, IOException {
		logger.info("In uploadSchemaScript()");
		ModelAndView modelAndView = new ModelAndView("chooseTables");

		DatabaseConnectInfo dbConnectInfo = databaseUtils.makeDefaultDatabaseConnectInfo();

		// Create a temporary user/schema in DB (as root) which will be dropped after applications zip is generated.
		long currTimeMillis = System.currentTimeMillis();
		String tempDatabaseId = "TEMPDB" + currTimeMillis;
		databaseUtils.createTempDbAndSchema(tempDatabaseId, dbConnectInfo);
		
		// Save DB connect info to session for later use
		changeConnectInfoToTempDb(dbConnectInfo, tempDatabaseId);
		session.setAttribute(SESSION_ATTRIB_DB_CONN_INFO, dbConnectInfo);
		
		// Execute the uploaded schema script, which only creates tables		
		String schemaCreateScript = new String(uploadedFile.getBytes());
		databaseUtils.createTempDbSchemaTables(dbConnectInfo, schemaCreateScript);
		
		// Get list of tables just created and put in ModelAndView
		setTableListAttribute(session, modelAndView);
		logger.info("Returning ModelAndView from uploadSchemaScript()");
		
		return modelAndView;
	}	

	@RequestMapping(value = "/savedbconnectInfo", method = RequestMethod.POST)
	public ModelAndView saveDatabaseConnectionInfo(DatabaseConnectInfo dbConnectInfo, HttpSession session) {
		logger.info("In saveDatabaseConnectionInfo()");
		ModelAndView modelAndView = new ModelAndView("chooseTables");
		List<String> errMsgList = getDBConnectInfoValidationErrors(dbConnectInfo, session);
		if(errMsgList.size() > 0) {
			modelAndView.setViewName("dbConnectInfo");
			modelAndView.addObject("errMsgList", errMsgList);
		}else {
			setTableListAttribute(session, modelAndView);
		}
		
		return modelAndView;
	}

	@RequestMapping(value = "/savetableselections", method = RequestMethod.POST,  produces="application/zip")
	public ModelAndView saveChosenTables(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("In saveChosenTables()");
		ModelAndView modelAndView = new ModelAndView("chooseOptions");
		List<String> tablesForBootApps = getTablesForBootApps(request);
		request.getSession().setAttribute(SESSION_ATTRIB_GEN_OPTIONS_INFO, tablesForBootApps);
		
		return modelAndView;
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savegenoptions", method = RequestMethod.POST,  produces="application/zip")
	public void saveOptionsInfo(GenerationOptions genOptions, HttpSession session,
							    HttpServletResponse response) throws IOException, SQLException {
		// Make the apps and download the zip, passing the chosen tables and generation options
		logger.info("In saveOptionsInfo()");
		List<String> tablesForBootApps = (List<String>)session.getAttribute(SESSION_ATTRIB_GEN_OPTIONS_INFO);
		DatabaseConnectInfo dbConnectInfo = (DatabaseConnectInfo)session.getAttribute(SESSION_ATTRIB_DB_CONN_INFO);	
		appsZipFactory.makeAppsZipFile(tablesForBootApps, genOptions, dbConnectInfo);

		response.setContentType("application/zip");
		response.addHeader("Content-Disposition", "attachment; filename=" + appsGenZipFileName);
		
		File appsZipFile = new File(appsGenWorkDir + "/" + appsGenZipFileName);
		Files.copy(appsZipFile.toPath(), response.getOutputStream());
		response.flushBuffer();
		
		// DROP the temp DB and schema for this user's session
		databaseUtils.dropTempDbAndSchema(dbConnectInfo.getDbName());
	}		
	
	private List<String> getTablesForBootApps(HttpServletRequest request){
		List<String> tablesForBootApps = new ArrayList<String>();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if(!StringUtils.isEmpty(paramName) && paramName.startsWith("selCrudBootApp|")) {
				String tableName = paramName.substring("selCrudBootApp|".length());
				tablesForBootApps.add(tableName);
			}
		}
		
		return tablesForBootApps;
	}
	
	private void setTableListAttribute(HttpSession session, ModelAndView modelAndView) {
		DatabaseConnectInfo dbConnectInfo = (DatabaseConnectInfo)session.getAttribute("dbConnectInfo");
		List<TableMetaData> tableMetaDataList = metaDataCollectionDAO.getTableMetaDataList(dbConnectInfo);
		modelAndView.addObject("tableMetaDataList", tableMetaDataList);
	}
	
	private List<String> getDBConnectInfoValidationErrors(DatabaseConnectInfo dbConnectInfo, HttpSession session){
		List<String> errorMsgList = new ArrayList<String>();
		if(StringUtils.isEmpty(dbConnectInfo.getUserSchema())) {
			errorMsgList.add("User/Schema is required");
		}else if(StringUtils.isEmpty(dbConnectInfo.getPassword())) {
			errorMsgList.add("Password is required");
		}else if(StringUtils.isEmpty(dbConnectInfo.getServerOrIP())) {
			errorMsgList.add("Server Name (or IP) is required");
		}else if(StringUtils.isEmpty(dbConnectInfo.getServerListenPort())) {
			errorMsgList.add("Server Listen Port is required");
		}else if(StringUtils.isEmpty(dbConnectInfo.getDbName())) {
			errorMsgList.add("DB Name is required");
		}else {
			try {
				validateConnection(dbConnectInfo, errorMsgList, session);
			}catch(Exception e) {
				errorMsgList.add("Error attempting to connect to database");
			}
		}
		
		return errorMsgList;
	}
	
	private void validateConnection(DatabaseConnectInfo dbConnectInfo, List<String> errorMsgList, HttpSession session) throws SQLException {
		// Add an error msg if we cannot establish a connection using the passed info
		if(!DatabaseVendor.MYSQL.getCode().equalsIgnoreCase(dbConnectInfo.getDbmsName())) {
			errorMsgList.add("Only MySQL DBMS is supported now");
		}else {
			metaDataCollectionDAO.attemptTestQuery(dbConnectInfo);

			// If we make it this far we're able to test connection. Save connect info in session for later.
	        session.setAttribute(SESSION_ATTRIB_DB_CONN_INFO, dbConnectInfo);	
		}
	}
	
	private void changeConnectInfoToTempDb(DatabaseConnectInfo dbConnectInfo, String tempDatabaseId) {
		dbConnectInfo.setDbName(tempDatabaseId);
		dbConnectInfo.setUserSchema(tempDatabaseId);
		dbConnectInfo.setPassword(tempDatabaseId);		
	}
		
}
