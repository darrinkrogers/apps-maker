package com.drogers.appsmaker.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.factory.javasrc.AttributeDataTypePartialsFactory;
import com.drogers.appsmaker.model.domain.ColumnMetaData;
import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;
import com.drogers.appsmaker.util.CodeNamingUtil;

@Component
public class TemplateVarMapFactory {
	
	@Autowired
	private AttributeDataTypePartialsFactory attributeDataTypePartialsFactory;
	
	@Autowired
	private CodeNamingUtil codeNamingUtil;		
	
	public Map<String, Object> makeForCrudBootAppJavaFile(String basePackageName, String tableName, String projectDirName) {
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("basePackageName", basePackageName + "." + (projectDirName.replace("-", "")));
		varMap.put("capitalizedTableName", codeNamingUtil.getCapitalizedEntityName(tableName));	
		
		return varMap;
	}	
	
	public Map<String, Object> makeForCrudBootRepositoryJavaFile(String basePackageName, String tableName,
																 String projectDirName, List<ColumnMetaData> columnMetaDataList, 
																 DatabaseVendor dbVendor) {
		Map<String, Object> varMap = makeForCrudBootAppJavaFile(basePackageName, tableName, projectDirName);	
		varMap.put("pkDataType", attributeDataTypePartialsFactory.makeJavaDataTypeForPK(columnMetaDataList, dbVendor));

		return varMap;
	}	
	
	public Map<String, Object> makeForCrudBootPomFile(String javaVersion, String basePackageName, String tableName) {
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("javaVersion", javaVersion);
		varMap.put("basePackageName", basePackageName);
		varMap.put("lowerCasedEntityName", codeNamingUtil.getLowerCasedEntityName(tableName));	
		varMap.put("capitalizedTableName", codeNamingUtil.getCapitalizedEntityName(tableName));	
		
		return varMap;
	}
	
	public Map<String, Object> makeForCrudBootAppPropertiesFile(DatabaseConnectInfo dbConnectInfo, DatabaseVendor dbVendor) {
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("dbName", dbConnectInfo.getDbName());
		varMap.put("dbUser", dbConnectInfo.getUserSchema());
		varMap.put("dbPswd", dbConnectInfo.getPassword());
		
		varMap.put("driverClassName", dbVendor.getDriverClassName());
		varMap.put("dialect", dbVendor.getDialect());
		
		return varMap;
	}
	
	public Map<String, Object> makeForLoggingConfigurationFile(String projectDirName) {
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("projectDirName", projectDirName);
		
		return varMap;
	}		
}
