package com.drogers.appsmaker.model.domain;

import java.io.Serializable;

public class DatabaseConnectInfo implements Serializable {
	
	private static final long serialVersionUID = -278834983045771262L;

	private String dbmsName;
	private String userSchema;
	private String password;
	private String serverOrIP;
	private String serverListenPort;
	private String dbName;
	
	public String getDbmsName() {
		return dbmsName;
	}
	public void setDbmsName(String dbmsName) {
		this.dbmsName = dbmsName;
	}
	public String getUserSchema() {
		return userSchema;
	}
	public void setUserSchema(String userSchema) {
		this.userSchema = userSchema;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServerOrIP() {
		return serverOrIP;
	}
	public void setServerOrIP(String serverOrIP) {
		this.serverOrIP = serverOrIP;
	}
	public String getServerListenPort() {
		return serverListenPort;
	}
	public void setServerListenPort(String serverListenPort) {
		this.serverListenPort = serverListenPort;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
}
