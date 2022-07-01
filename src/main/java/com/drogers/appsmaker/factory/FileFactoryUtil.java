package com.drogers.appsmaker.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

@Component
public class FileFactoryUtil {
	
	public File makeFileWithContent(String fileName, String content) throws IOException {
		File file = new File(fileName);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(content);
		fileWriter.close();
		
		return file;		
	}
	
	public void addFileToZip(ZipOutputStream zipOut, File file, String zipEntryName) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(zipEntryName);
		zipOut.putNextEntry(zipEntry);			
	
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fileInputStream.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fileInputStream.close();		
	}		
}
