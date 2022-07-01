package com.drogers.appsmaker.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CodeNamingUtil {
	
	public String getCapitalizedEntityName(String tableName) {
		String capitalizedEntityName = "";
		String[] tableNameTokens = tableName.split("_");
		
		if(tableNameTokens == null || tableNameTokens.length < 1) {
			capitalizedEntityName = StringUtils.capitalize(tableName);
		}else {
			for(String tableNamePart: tableNameTokens) {
				capitalizedEntityName += StringUtils.capitalize(tableNamePart);
			}			
		}
		
		return capitalizedEntityName;
	}
	
	public String getLowerCamelCasedEntityName(String tableName) {
		String lowerCamelCasedEntityName = "";
		String[] tableNameTokens = tableName.split("_");
		
		if(tableNameTokens == null || tableNameTokens.length < 1) {
			lowerCamelCasedEntityName = tableName.toLowerCase();
		}else {
			int iterationCount = 1;
			for(String tableNamePart: tableNameTokens) {
				lowerCamelCasedEntityName +=
						(iterationCount == 1?tableNamePart.toLowerCase():StringUtils.capitalize(tableNamePart));
				iterationCount++;
			}			
		}
		
		return lowerCamelCasedEntityName;
	}
	
	public String getLowerCasedEntityName(String tableName) {
		String lowerCasedEntityName = tableName.replace("_", "");

		return lowerCasedEntityName.toLowerCase();
	}	
}
