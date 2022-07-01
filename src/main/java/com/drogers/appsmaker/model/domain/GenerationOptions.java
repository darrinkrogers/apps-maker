package com.drogers.appsmaker.model.domain;

public class GenerationOptions {
	
	private String basePackageName;
	private String javaVersion;
	
	public String getBasePackageName() {
		return basePackageName;
	}
	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}
	public String getJavaVersion() {
		return javaVersion;
	}
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
}
