package com.drogers.appsmaker.factory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;
import com.drogers.appsmaker.model.domain.GenerationOptions;
import com.drogers.appsmaker.util.CodeNamingUtil;

/**
 * Makes a .zip file containing a directory for each generated application. Each folder
 * represents a front-end application (1 for all tables) or a CRUD/Rest services spring boot application
 * (1 for each table selected).
 * 
 * @author drogers
 *
 */

@Component
public class GeneratedAppsZipFactory {

	@Value("${apps.gen.work.dir}")
	private String appsGenWorkDir;
	
	@Value("${apps.gen.zip.file.name}")
	private String appsGenZipFileName;	
	
	@Autowired
	private CrudBootAppFactory crudBootAppFactory;
	
	@Autowired
	private CodeNamingUtil codeNamingUtil;
	
	public void makeAppsZipFile(List<String> tablesForBootApps, GenerationOptions genOptions,
								DatabaseConnectInfo dbConnectInfo) throws IOException {
		
		// Clear the work directory
		File workDir = new File(appsGenWorkDir);
		FileUtils.cleanDirectory(workDir);		

		// Create a new apps.zip file in work directory
		FileOutputStream fos = new FileOutputStream(appsGenWorkDir + "/" + appsGenZipFileName);		
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		
		// For each table:
		// -Create a folder name "<tableName>-api" in work dir, and add directories/files for the boot app & add to zip file
		// -If user chose to create a UI app for table, create a folder in zip file for UI app with needed directories/files
		String javaVersion = StringUtils.isEmpty(genOptions.getJavaVersion())?"1.13":genOptions.getJavaVersion();
		String basePackageName = StringUtils.isEmpty(genOptions.getBasePackageName())?"com.generic":genOptions.getBasePackageName();
		
		for(String tableName: tablesForBootApps) {
			String projectDirName = codeNamingUtil.getLowerCasedEntityName(tableName) + "-" + "api";
			Files.createDirectory(Paths.get(appsGenWorkDir + "/" + projectDirName));
			Files.createDirectory(Paths.get(appsGenWorkDir + "/" + projectDirName, "/src"));
			Files.createDirectory(Paths.get(appsGenWorkDir + "/" + projectDirName, "/src/main"));
			Files.createDirectory(Paths.get(appsGenWorkDir + "/" + projectDirName, "/src/main/resources"));
			String basePackageJavaDirName = makeBasePackageJavaDir(projectDirName, basePackageName);
			addCrudBootAppFolderAndContentsToZip(zipOut, tableName, javaVersion, basePackageName, 
												 basePackageJavaDirName, projectDirName, dbConnectInfo);
		}
		
		zipOut.close();
		fos.close();		
	}
	
	private void addCrudBootAppFolderAndContentsToZip(ZipOutputStream zipOut, String tableName,
												  	  String javaVersion, String basePackageName,
												  	  String basePackageJavaDirName, String projectDirName,
												  	  DatabaseConnectInfo dbConnectInfo) throws IOException {
		crudBootAppFactory.makeSpringBootAppFolderStructure(zipOut, tableName, javaVersion, basePackageName,
															basePackageJavaDirName, projectDirName, dbConnectInfo);
	}
	
	private String makeBasePackageJavaDir(String projectDirName, String basePackageName) throws IOException {
		
		Files.createDirectory(Paths.get(appsGenWorkDir + "/" + projectDirName, "/src/main/java"));
		String[] basePackageDirsArray = basePackageName.split("\\.");
		String basePackageJavaDirName = appsGenWorkDir + "/" + projectDirName + "/src/main/java";
		for(int i=0; i<basePackageDirsArray.length; i++) {
			Files.createDirectory(Paths.get(basePackageJavaDirName, "/" + basePackageDirsArray[i]));
			basePackageJavaDirName = basePackageJavaDirName + "/" + basePackageDirsArray[i];
		}
		basePackageJavaDirName += "/" + projectDirName.replace("-", "");
		Files.createDirectory(Paths.get(basePackageJavaDirName));
		
		return basePackageJavaDirName;
	}

}
