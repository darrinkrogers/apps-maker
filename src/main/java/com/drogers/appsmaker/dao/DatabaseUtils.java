package com.drogers.appsmaker.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;

@Component
public class DatabaseUtils {
	
	public static final String DEFAULT_DBMS = DatabaseVendor.MYSQL.getCode();
	public static final String DEFAULT_DB_NAME = "movies";
	public static final String DEFAULT_USER_SCHEMA = "schemacreator";
	public static final String DEFAULT_PSWD = "schemacreator877"; // TODO: Store encrypted value & decrypt b4 trying
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_PORT = "3306";
	
	private Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
	
	public DatabaseConnectInfo makeDatabaseConnectInfo(String dbmsName, String dbName, String userSchema,
													   String password, String host, String port) {
		DatabaseConnectInfo dbConnectInfo = new DatabaseConnectInfo();
	
		dbConnectInfo.setDbmsName(dbmsName);
		dbConnectInfo.setDbName(dbName);
		dbConnectInfo.setUserSchema(userSchema);
		dbConnectInfo.setPassword(password);
		dbConnectInfo.setServerOrIP(host);
		dbConnectInfo.setServerListenPort(port);		
		
		return dbConnectInfo;
	}

	public JdbcTemplate makeJdbcTemplate(DatabaseConnectInfo dbConnectInfo) {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(DatabaseVendor.forCode(dbConnectInfo.getDbmsName()).getDriverClassName());
        dataSourceBuilder.url("jdbc:" + dbConnectInfo.getDbmsName() + "://" + dbConnectInfo.getServerOrIP().trim() + ":" +
        					  dbConnectInfo.getServerListenPort() + "/" + dbConnectInfo.getDbName().trim());
        dataSourceBuilder.username(dbConnectInfo.getUserSchema().trim());
        dataSourceBuilder.password(dbConnectInfo.getPassword().trim());
        
        return new JdbcTemplate(dataSourceBuilder.build(), false);		
	}
	
	public void createTempDbAndSchema(String tempDatabaseId, DatabaseConnectInfo dbConnectInfo) {
		JdbcTemplate jdbcTemplate = makeJdbcTemplate(dbConnectInfo);
		logger.info("In createTempDbAndSchema(). jdbcTemplate created for temp DB/schema creation.");
		jdbcTemplate.execute("select * from movie");
		jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS " + tempDatabaseId);
		jdbcTemplate.execute("CREATE USER '" + tempDatabaseId + "'@'localhost' IDENTIFIED BY '" + tempDatabaseId + "'");
		jdbcTemplate.execute("GRANT ALL PRIVILEGES ON " + tempDatabaseId + ".* TO '" + tempDatabaseId + "'@'localhost' WITH GRANT OPTION");
		logger.info("createTempDbAndSchema(). temp DB and schema created.");
	}
	
	public void createTempDbSchemaTables(DatabaseConnectInfo dbConnectInfo, String schemaCreateScript) throws SQLException {
		JdbcTemplate jdbcTemplateForTempUser = makeJdbcTemplate(dbConnectInfo);
		Connection tempDBConnection = jdbcTemplateForTempUser.getDataSource().getConnection();
		ScriptUtils.executeSqlScript(tempDBConnection, new ByteArrayResource(schemaCreateScript.getBytes()));
		tempDBConnection.close();
	}
	
	public void dropTempDbAndSchema(String tempDatabaseId) throws SQLException {
		JdbcTemplate jdbcTemplate = makeJdbcTemplate(makeDefaultDatabaseConnectInfo());
		jdbcTemplate.execute("DROP USER '" + tempDatabaseId + "'@'localhost'");
		jdbcTemplate.execute("DROP DATABASE " + tempDatabaseId);
	}
	
	public DatabaseConnectInfo makeDefaultDatabaseConnectInfo() {
		return makeDatabaseConnectInfo(DEFAULT_DBMS, DEFAULT_DB_NAME, DEFAULT_USER_SCHEMA,
									   DEFAULT_PSWD, DEFAULT_HOST, DEFAULT_PORT);		
	}
}
