package com.drogers.appsmaker.factory.javasrc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drogers.appsmaker.dao.MetaDataCollectionDAO;
import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.model.domain.ColumnMetaData;
import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;
import com.drogers.appsmaker.model.domain.InstanceVariable;
import com.drogers.appsmaker.service.templating.TemplateService;
import com.drogers.appsmaker.util.CodeNamingUtil;

/**
 * Makes and returns a .java file which represents the JPA annotated domain model. This is
 * the main entity that will be persisted in the generated CRUD Spring Boot application.
 * Initial support limited to tables with single column PK's, and one attribute for 
 * every remaining column in source table.
 * 
 * @author drogers
 *
 */

@Component
public class CrudBootDomainModelFactory {

	private static final String TEMPLATE_DIR = "templates/crud-app/java";
	
	@Autowired
	private TemplateService templateService;	

	@Autowired
	private MetaDataCollectionDAO metaDataCollectionDAO;	

	@Autowired
	private AttributeDataTypePartialsFactory dataTypePartialsFactory;
	
	@Autowired
	private CodeNamingUtil codeNamingUtil;		
	
	public File makeDomainModelJavaFile(DatabaseConnectInfo dbConnectInfo, String basePackageName, String basePackageJavaDirName, 
			String tableName, List<ColumnMetaData> columnMetaDataList, DatabaseVendor dbVendor, String projectDirName) throws IOException {
		Map<String, Object> varMap = makeTemplateVarMap(dbConnectInfo, basePackageName, tableName, dbVendor, columnMetaDataList, projectDirName);
		String domainModelJavaFileContent = templateService.makeContent(TEMPLATE_DIR + "/DomainModel-java.vm", varMap);
		Files.createDirectories(Paths.get(basePackageJavaDirName + "/model/domain"));
		String fileName = basePackageJavaDirName + "/model/domain/" + codeNamingUtil.getCapitalizedEntityName(tableName) +  ".java";
		
		return makeFileWithContent(fileName, domainModelJavaFileContent);
	}

	private File makeFileWithContent(String fileName, String content) throws IOException {
		File domainModelJavaFile = new File(fileName);
		FileWriter domainModelJavaFileWriter = new FileWriter(domainModelJavaFile);
		domainModelJavaFileWriter.write(content);
		domainModelJavaFileWriter.close();
		
		return domainModelJavaFile;		
	}	
	
	// TODO: Add support to make PK different java type, and multi-column PK's (current support: 1 column Integer)
	// TODO: Add support for different JPA ID generator (which can be DB vendor specific: strategy pattern)
	public Map<String, Object> makeTemplateVarMap(DatabaseConnectInfo dbConnectInfo, String basePackageName,
												  String tableName, DatabaseVendor dbVendor, List<ColumnMetaData> columnMetaDataList,
												  String projectDirName) {
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("basePackageName", basePackageName + "." + (projectDirName.replace("-", "")));
		varMap.put("capitalizedEntityName", codeNamingUtil.getCapitalizedEntityName(tableName));	
		
		String pkAttributeName = makePkAttributeName(columnMetaDataList);
		varMap.put("tableName", tableName);
		varMap.put("pkAttributeName", pkAttributeName);
		varMap.put("pkDataType",  dataTypePartialsFactory.makeJavaDataTypeForPK(columnMetaDataList, dbVendor));
		varMap.put("instanceVariableList", makeInstanceVariableList(columnMetaDataList, dbVendor));
		
		return varMap;
	}
	
	private String makePkAttributeName(List<ColumnMetaData> columnMetaDataList) {
		String attribName = "";
		for(ColumnMetaData colMetaData: columnMetaDataList) {
			if(colMetaData.getIsPkColumn() == 1) {
				attribName = metaDataCollectionDAO.getCamelCasedAttributeName(colMetaData.getName());
			}
		}
				
		return attribName;
	}
	
	private List<InstanceVariable> makeInstanceVariableList(List<ColumnMetaData> columnMetaDataList, DatabaseVendor dbVendor){
		List<InstanceVariable> instanceVariableList = new ArrayList<InstanceVariable>();
		for(ColumnMetaData columnMetaData: columnMetaDataList) {
			if(columnMetaData.getIsPkColumn() != 1) {
				InstanceVariable var = new InstanceVariable();
				var.setScope("private");
				var.setDataType(dataTypePartialsFactory.makeFullyQualifiedDataTypeName(columnMetaData, dbVendor));
				var.setName(metaDataCollectionDAO.getCamelCasedAttributeName(columnMetaData.getName()));
				var.setColName(columnMetaData.getName());
				instanceVariableList.add(var);
			}
		}
		
		return instanceVariableList;
	}

}
