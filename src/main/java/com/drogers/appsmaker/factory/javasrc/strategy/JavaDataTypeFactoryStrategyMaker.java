package com.drogers.appsmaker.factory.javasrc.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.factory.javasrc.strategy.impl.JavaDataTypeFromDBTypeFactoryMySQLImpl;

@Component
public class JavaDataTypeFactoryStrategyMaker {

	@Autowired
	private JavaDataTypeFromDBTypeFactoryMySQLImpl javaDataTypeFromDBTypeFactoryMySQLImpl;
	
	public JavaDataTypeFromDBTypeFactory makeFactoryStrategy(DatabaseVendor databaseVendor) {
		
		JavaDataTypeFromDBTypeFactory javaDataTypeFactory = null;
		
		if(DatabaseVendor.MYSQL.getCode().equals(databaseVendor.getCode())) {
			javaDataTypeFactory = javaDataTypeFromDBTypeFactoryMySQLImpl;
		}
		
		return javaDataTypeFactory;
	}
}
