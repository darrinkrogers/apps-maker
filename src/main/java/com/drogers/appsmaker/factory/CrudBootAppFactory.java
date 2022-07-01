package com.drogers.appsmaker.factory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.drogers.appsmaker.dao.MetaDataCollectionDAO;
import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.factory.javasrc.CrudBootDomainModelFactory;
import com.drogers.appsmaker.factory.javasrc.CrudBootRestControllerFactory;
import com.drogers.appsmaker.model.domain.ColumnMetaData;
import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;
import com.drogers.appsmaker.service.templating.TemplateService;
import com.drogers.appsmaker.util.CodeNamingUtil;

/**
 * Makes a Spring Boot app project directory structure and adds it to the passed in ZipOutputStream. 
 * 
 * @author drogers
 *
 */

@Component
public class CrudBootAppFactory {

	private static final String TEMPLATE_DIR = "templates/crud-app";
	private static final String JAVA_TEMPLATE_DIR = TEMPLATE_DIR + "/java";
	private static final String RESOURCE_TEMPLATE_DIR = TEMPLATE_DIR + "/resources";
	
	@Value("${apps.gen.work.dir}")
	private String appsGenWorkDir;	
	
	@Autowired
	private TemplateService templateService;	
	
	@Autowired
	private CrudBootDomainModelFactory domainModelFactory;
	
	@Autowired
	private CrudBootRestControllerFactory crudBootRestControllerFactory;
	
	@Autowired
	private TemplateVarMapFactory templateVarMapFactory;
	
	@Autowired
	private FileFactoryUtil fileFactoryUtil;	
	
	@Autowired
	private MetaDataCollectionDAO metaDataCollectionDAO;	
	
	@Autowired
	private CodeNamingUtil codeNamingUtil;
	
	public void makeSpringBootAppFolderStructure(ZipOutputStream zipOut, String tableName,
		  	  									 String javaVersion, String basePackageName,
		  	  									 String basePackageJavaDirName, String projectDirName,
		  	  									 DatabaseConnectInfo dbConnectInfo) throws IOException {
		
		DatabaseVendor dbVendor = DatabaseVendor.forCode(dbConnectInfo.getDbmsName());
		List<ColumnMetaData> columnMetaDataList = metaDataCollectionDAO.getColumnMetaDataForTable(tableName, dbConnectInfo);
		
		File pomXmlFile =  makePomXmlFile(javaVersion, basePackageName, projectDirName, tableName) ;
		addPomXmlFile(zipOut, pomXmlFile, projectDirName);
				
		File applicationPropertiesFile = makeApplicationPropertiesFile(projectDirName, dbConnectInfo, dbVendor);
		addApplicationPropertiesFile(zipOut, applicationPropertiesFile, projectDirName);
		
		String basePackageJavaZipDirName = basePackageJavaDirName.substring((appsGenWorkDir + "/").length());
		File bootApplicationJavaFile = makeApplicationJavaFile(basePackageName, basePackageJavaDirName, tableName, projectDirName);
		addApplicationJavaFile(zipOut, bootApplicationJavaFile, basePackageJavaZipDirName);
		
		File loggingConfigurationFile = makeLoggingConfigurationFile(projectDirName);
		addloggingConfigurationFile(zipOut, loggingConfigurationFile, projectDirName);
		
		File repositoryJavaFile = makeRepositoryJavaFile(basePackageName, basePackageJavaDirName, tableName, projectDirName,
														 columnMetaDataList, dbVendor);
		fileFactoryUtil.addFileToZip(zipOut, repositoryJavaFile, basePackageJavaZipDirName + "/repository/" + repositoryJavaFile.getName());
		
		File domainModelJavaFile = domainModelFactory.makeDomainModelJavaFile(dbConnectInfo, basePackageName, basePackageJavaDirName,
																			  tableName, columnMetaDataList, dbVendor, projectDirName);
		fileFactoryUtil.addFileToZip(zipOut, domainModelJavaFile, basePackageJavaZipDirName + "/model/domain/" + domainModelJavaFile.getName());
		
		File restControllerJavaFile = crudBootRestControllerFactory.makeRestControllerJavaFile(basePackageName, basePackageJavaDirName,
																							   tableName, projectDirName,
																							   columnMetaDataList, dbVendor);
		fileFactoryUtil.addFileToZip(zipOut, restControllerJavaFile, basePackageJavaZipDirName + "/controller/" + restControllerJavaFile.getName());
	}
	
	private void addloggingConfigurationFile(ZipOutputStream zipOut, File loggingConfigurationFile, String projectDirName) throws IOException {
		fileFactoryUtil.addFileToZip(zipOut, loggingConfigurationFile, projectDirName + "/src/main/resources/" + loggingConfigurationFile.getName());
	}		
	
	private void addApplicationJavaFile(ZipOutputStream zipOut, File applicationJavaFile, String basePackageJavaZipDirName) throws IOException {
		fileFactoryUtil.addFileToZip(zipOut, applicationJavaFile, basePackageJavaZipDirName + "/" + applicationJavaFile.getName());
	}	
	
	private void addApplicationPropertiesFile(ZipOutputStream zipOut, File applicationPropertiesFile, String projectDirName) throws IOException {
		fileFactoryUtil.addFileToZip(zipOut, applicationPropertiesFile, projectDirName + "/src/main/resources/" + applicationPropertiesFile.getName());
	}	
	
	private void addPomXmlFile(ZipOutputStream zipOut, File pomXmlFile, String projectDirName) throws IOException {
		fileFactoryUtil.addFileToZip(zipOut, pomXmlFile, projectDirName + "/" + pomXmlFile.getName());
	}	
	
	private File makeApplicationJavaFile(String basePackageName, String basePackageJavaDirName, String tableName, String projectDirName) throws IOException {
		Map<String, Object> varMap = templateVarMapFactory.makeForCrudBootAppJavaFile(basePackageName, tableName, projectDirName);
		String appJavaFileContent = templateService.makeContent(JAVA_TEMPLATE_DIR + "/ApiApplication-java.vm", varMap);
		String fileName = basePackageJavaDirName + "/" + codeNamingUtil.getCapitalizedEntityName(tableName) + "ApiApplication.java";
		
		return fileFactoryUtil.makeFileWithContent(fileName, appJavaFileContent);
	}	
	
	private File makeRepositoryJavaFile(String basePackageName, String basePackageJavaDirName, 
										String tableName, String projectDirName,
										List<ColumnMetaData> columnMetaDataList, DatabaseVendor dbVendor) throws IOException {
		Map<String, Object> varMap = templateVarMapFactory.makeForCrudBootRepositoryJavaFile(basePackageName, tableName,
																							 projectDirName, columnMetaDataList, dbVendor);
		String repoJavaFileContent = templateService.makeContent(JAVA_TEMPLATE_DIR + "/" +  "Repository-java.vm", varMap);
		String fileName = basePackageJavaDirName + "/repository/" + codeNamingUtil.getCapitalizedEntityName(tableName) + "Repository.java";
		Files.createDirectory(Paths.get(basePackageJavaDirName + "/repository"));
		
		return fileFactoryUtil.makeFileWithContent(fileName, repoJavaFileContent);
	}	
	
	private File makeApplicationPropertiesFile(String projectDirName, DatabaseConnectInfo dbConnectInfo, DatabaseVendor dbVendor) throws IOException {
		Map<String, Object> varMap = templateVarMapFactory.makeForCrudBootAppPropertiesFile(dbConnectInfo, dbVendor);
		String appPropertiesFileContent = templateService.makeContent(RESOURCE_TEMPLATE_DIR + "/application-properties.vm", varMap);
		String fileName = appsGenWorkDir + "/" + projectDirName + "/src/main/resources/application.properties";
		
		return fileFactoryUtil.makeFileWithContent(fileName, appPropertiesFileContent);
	}		
	
	private File makePomXmlFile(String javaVersion, String basePackageName, String projectDirName, String tableName) throws IOException {
		Map<String, Object> varMap = templateVarMapFactory.makeForCrudBootPomFile(javaVersion, basePackageName, tableName);
		String pomContent = templateService.makeContent(TEMPLATE_DIR + "/pom-xml.vm", varMap);
		String fileName = appsGenWorkDir + "/" + projectDirName + "/pom.xml";
		
		return fileFactoryUtil.makeFileWithContent(fileName, pomContent);
	}

	private File makeLoggingConfigurationFile(String projectDirName) throws IOException {
		Map<String, Object> varMap = templateVarMapFactory.makeForLoggingConfigurationFile(projectDirName);
		String loggingConfigurationFileContent = templateService.makeContent(RESOURCE_TEMPLATE_DIR + "/logback-spring-xml.vm", varMap);
		String fileName = appsGenWorkDir + "/" + projectDirName + "/src/main/resources/logback-spring.xml";
		
		return fileFactoryUtil.makeFileWithContent(fileName, loggingConfigurationFileContent);
	}		
	
}
