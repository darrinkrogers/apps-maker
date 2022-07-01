package com.drogers.appsmaker.enums;

public enum DatabaseVendor {
	
	MYSQL ("mysql", "MySQL", "com.mysql.cj.jdbc.Driver", "org.hibernate.dialect.MySQLDialect");
	
	private String code;
	private String description;
	private String driverClassName;
	private String dialect;
	
	DatabaseVendor(String code, String description, String driverClassName, String dialect){
		this.code = code;
		this.description = description;
		this.driverClassName = driverClassName;
		this.dialect = dialect;
	}
	
	public static DatabaseVendor forCode(String code) {
		DatabaseVendor vendorForCode = null;
		for(DatabaseVendor dbVendor: values()) {
			if(code.equalsIgnoreCase(dbVendor.getCode())) {
				vendorForCode = dbVendor;
			}
		}
		
		return vendorForCode;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public String getDialect() {
		return dialect;
	}
}
