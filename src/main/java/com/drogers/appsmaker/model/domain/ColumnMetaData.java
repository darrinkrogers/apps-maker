package com.drogers.appsmaker.model.domain;

public class ColumnMetaData {
	
	private String name;
	private String dataType;
	private String javaObjectType;
	private Integer isPkColumn;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getJavaObjectType() {
		return javaObjectType;
	}
	public void setJavaObjectType(String javaObjectType) {
		this.javaObjectType = javaObjectType;
	}
	public Integer getIsPkColumn() {
		return isPkColumn;
	}
	public void setIsPkColumn(Integer isPkColumn) {
		this.isPkColumn = isPkColumn;
	}
	
}
