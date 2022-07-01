package com.drogers.appsmaker.factory.javasrc.strategy.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.drogers.appsmaker.factory.javasrc.strategy.JavaDataTypeFromDBTypeFactory;
import com.drogers.appsmaker.model.domain.ColumnMetaData;

@Component
public class JavaDataTypeFromDBTypeFactoryMySQLImpl implements JavaDataTypeFromDBTypeFactory {
	
	// TODO: Change those that are based on: BIT size, UNSIGNED, 
	@Override
	public String makeJavaDataTypeFrom(ColumnMetaData columnMetaData) {
		String javaDataType = null;
		
		switch(columnMetaData.getDataType().toUpperCase())
		{
			case "BIT":
				javaDataType = "java.lang.Boolean";
				break;
			case "TINYINT":
				javaDataType = "java.lang.Boolean";
				break;
			case "BOOL":
				javaDataType = "java.lang.Boolean";
				break;
			case "BOOLEAN":
				javaDataType = "java.lang.Boolean";
				break;
			case "SMALLINT":
				javaDataType = "java.lang.Integer";
				break;
			case "MEDIUMINT":
				javaDataType = "java.lang.Integer";
				break;
			case "INT":
				javaDataType = "java.lang.Integer";
				break;
			case "INTEGER":
				javaDataType = "java.lang.Integer";
				break;
			case "BIGINT":
				javaDataType = "java.math.BigInteger";
				break;
			case "FLOAT":
				javaDataType = "java.lang.Float";
				break;
			case "DOUBLE":
				javaDataType = "java.lang.Double";
				break;
			case "DECIMAL":
				javaDataType = "java.math.BigDecimal";
				break;
			case "DATE":
				javaDataType = "java.sql.Date";
				break;
			case "DATETIME":
				javaDataType = "java.sql.Timestamp";
				break;
			case "TIMESTAMP":
				javaDataType = "java.sql.Timestamp";
				break;
			case "TIME":
				javaDataType = "java.sql.Time";
				break;
			case "YEAR":
				javaDataType = "java.sql.Date";
				break;
			case "CHAR":
				javaDataType = "java.lang.String";
				break;
			case "VARCHAR":
				javaDataType = "java.lang.String";
				break;
			case "BINARY":
				javaDataType = "byte[]";
				break;
			case "VARBINARY":
				javaDataType = "byte[]";
				break;
			case "TINYBLOB":
				javaDataType = "byte[]";
				break;
			case "TINYTEXT":
				javaDataType = "java.lang.String";
				break;
			case "BLOB":
				javaDataType = "byte[]";
				break;
			case "TEXT":
				javaDataType = "java.lang.String";
				break;
			case "MEDIUMBLOB":
				javaDataType = "byte[]";
				break;
			case "MEDIUMTEXT":
				javaDataType = "java.lang.String";
				break;
			case "LONGBLOB":
				javaDataType = "byte[]";
				break;
			case "LONGTEXT":
				javaDataType = "java.lang.String";
				break;
			case "ENUM":
				javaDataType = "java.lang.String";
				break;
			case "SET":
				javaDataType = "java.lang.String";
				break;
			default:
				javaDataType = "java.lang.Object";
		}
		
		return javaDataType;
	}

	@Override
	public String makeJavaDataTypeForPK(List<ColumnMetaData> columnMetaDataList) {
		String dataTypeName = "";
		for(ColumnMetaData colMetaData: columnMetaDataList) {
			if(colMetaData.getIsPkColumn() == 1) {
				dataTypeName = makeJavaDataTypeFrom(colMetaData);
			}
		}
				
		return dataTypeName;
	}

}
