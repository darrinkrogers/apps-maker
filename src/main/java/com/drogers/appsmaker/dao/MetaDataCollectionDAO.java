package com.drogers.appsmaker.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.drogers.appsmaker.model.domain.ColumnMetaData;
import com.drogers.appsmaker.model.domain.DatabaseConnectInfo;
import com.drogers.appsmaker.model.domain.TableMetaData;

@Service
public class MetaDataCollectionDAO {

	public List<TableMetaData> getTableMetaDataList(DatabaseConnectInfo dbConnectInfo) {
		JdbcTemplate jdbcTemplate = makeJdbcTemplate(dbConnectInfo);
		List<TableMetaData> tableMetaDataList = 
				(List<TableMetaData>)jdbcTemplate.query("select table_name as tableName, table_rows as rowCount, " + 
														"       create_time as createDateTime " + 
														"from information_schema.tables " + 
														"where table_schema = ?", 
														new Object[] {dbConnectInfo.getDbName()},
						new BeanPropertyRowMapper<TableMetaData>(TableMetaData.class));
		return tableMetaDataList;		
	}

	public List<ColumnMetaData> getColumnMetaDataForTable(String tableName, DatabaseConnectInfo dbConnectInfo){
		JdbcTemplate jdbcTemplate = makeJdbcTemplate(dbConnectInfo);
		List<ColumnMetaData> columnMetaDataList = 
				(List<ColumnMetaData>)jdbcTemplate.query("select column_name as name, data_type as dataType, " + 
														 "  	 if(column_key = 'PRI' , 1, 0) isPkColumn " + 
														 "	from information_schema.columns " + 
														 "	where table_schema = ? " + 
														 "	and table_name = ?", 
														new Object[] {dbConnectInfo.getDbName(), tableName},
						new BeanPropertyRowMapper<ColumnMetaData>(ColumnMetaData.class));		
		
		return columnMetaDataList;
	}	

	public void attemptTestQuery(DatabaseConnectInfo dbConnectInfo) {

		JdbcTemplate jdbcTemplate = makeJdbcTemplate(dbConnectInfo);
        jdbcTemplate.execute("select 1");		
	}

	
	public String getCamelCasedAttributeName(String colName) {
		String attribName = "";
		if(!StringUtils.isEmpty(colName)) {
			String[] tokensFromHyphens = StringUtils.split(colName, "_");
			for(String token: tokensFromHyphens) {
				if(StringUtils.isEmpty(attribName)) {
					attribName = token.toLowerCase();
				}else {
					attribName += StringUtils.capitalize(token);
				}
			}
		}
		
		return attribName;
	}
	
	private JdbcTemplate makeJdbcTemplate(DatabaseConnectInfo dbConnectInfo) {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver"); // TODO: Refactor to user entered/selected driver
        dataSourceBuilder.url("jdbc:mysql://" + dbConnectInfo.getServerOrIP().trim() + ":" +
        					  dbConnectInfo.getServerListenPort() + "/" + dbConnectInfo.getDbName().trim());
        dataSourceBuilder.username(dbConnectInfo.getUserSchema().trim());
        dataSourceBuilder.password(dbConnectInfo.getPassword().trim());
        
        return new JdbcTemplate(dataSourceBuilder.build(), false);		
	}	
}
