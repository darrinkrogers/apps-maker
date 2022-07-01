package com.drogers.appsmaker.factory.javasrc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.factory.FileFactoryUtil;
import com.drogers.appsmaker.model.domain.ColumnMetaData;
import com.drogers.appsmaker.service.templating.TemplateService;
import com.drogers.appsmaker.util.CodeNamingUtil;

/**
 * Makes and returns a .java file which represents the Rest controller for the domain entity.
 * The controller interfaces with the generated repository. Supported operations for the entity
 * are: get by ID, get all, create, update, delete.
 * 
 * @author drogers
 *
 */

@Component
public class CrudBootRestControllerFactory {

	private static final String TEMPLATE_DIR = "templates/crud-app/java";
	private static final String COMPONENT_TYPE = "Controller";
	
	@Autowired
	private TemplateService templateService;	
	
	@Autowired
	private FileFactoryUtil fileFactoryUtil;
	
	@Autowired
	private AttributeDataTypePartialsFactory attributeDataTypePartialsFactory;
	
	@Autowired
	private CodeNamingUtil codeNamingUtil;	
	
	public File makeRestControllerJavaFile(String basePackageName, String basePackageJavaDirName, 
			String tableName, String projectDirName,
			List<ColumnMetaData> columnMetaDataList, DatabaseVendor dbVendor) throws IOException {
		Map<String, Object> varMap = makeTemplateVarMap(basePackageName, tableName, projectDirName, columnMetaDataList, dbVendor);
		String domainModelJavaFileContent = templateService.makeContent(TEMPLATE_DIR + "/Rest" + COMPONENT_TYPE + "-java.vm", varMap);
		Files.createDirectories(Paths.get(basePackageJavaDirName + "/" + COMPONENT_TYPE.toLowerCase()));
		String fileName = basePackageJavaDirName + "/" + COMPONENT_TYPE.toLowerCase() + "/" +
				codeNamingUtil.getCapitalizedEntityName(tableName) + COMPONENT_TYPE + ".java";
		
		return fileFactoryUtil.makeFileWithContent(fileName, domainModelJavaFileContent);
	}
	
	// TODO: Add support for GET of collection with filters (current support gets all entity instances)
	// TODO: Add paging support for GET of collection (current support will return all entity instances)
	public Map<String, Object> makeTemplateVarMap(String basePackageName, String tableName, String projectDirName,
			List<ColumnMetaData> columnMetaDataList, DatabaseVendor dbVendor) {
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("basePackageName", basePackageName + "." + (projectDirName.replace("-", "")));
		varMap.put("capitalizedEntityName", codeNamingUtil.getCapitalizedEntityName(tableName));	
		varMap.put("lowerCasedEntityName", codeNamingUtil.getLowerCasedEntityName(tableName));
		varMap.put("lowerCamelCasedEntityName", codeNamingUtil.getLowerCamelCasedEntityName(tableName));
		varMap.put("pkDataType", attributeDataTypePartialsFactory.makeJavaDataTypeForPK(columnMetaDataList, dbVendor));
		
		return varMap;
	}
}
