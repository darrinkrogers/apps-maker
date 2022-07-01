package com.drogers.appsmaker.factory.javasrc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drogers.appsmaker.enums.DatabaseVendor;
import com.drogers.appsmaker.factory.javasrc.strategy.JavaDataTypeFactoryStrategyMaker;
import com.drogers.appsmaker.factory.javasrc.strategy.JavaDataTypeFromDBTypeFactory;
import com.drogers.appsmaker.model.domain.ColumnMetaData;

@Component
public class AttributeDataTypePartialsFactory {
	
	@Autowired
	private JavaDataTypeFactoryStrategyMaker javaDataTypeFactoryStrategyMaker;	
	
	public String makeFullyQualifiedDataTypeName(ColumnMetaData columnMetaData, DatabaseVendor dbVendor) {
		JavaDataTypeFromDBTypeFactory factory = javaDataTypeFactoryStrategyMaker.makeFactoryStrategy(dbVendor);
		
		return factory.makeJavaDataTypeFrom(columnMetaData);
	}
	
	public String makeJavaDataTypeForPK(List<ColumnMetaData> columnMetaDataList, DatabaseVendor dbVendor) {
		JavaDataTypeFromDBTypeFactory factory = javaDataTypeFactoryStrategyMaker.makeFactoryStrategy(dbVendor);
		
		return factory.makeJavaDataTypeForPK(columnMetaDataList);
	}
}
