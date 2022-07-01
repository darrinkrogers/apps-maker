package com.drogers.appsmaker.factory.javasrc.strategy;

import java.util.List;

import com.drogers.appsmaker.model.domain.ColumnMetaData;

public interface JavaDataTypeFromDBTypeFactory {

	public String makeJavaDataTypeFrom(ColumnMetaData columnMetaData);
	public String makeJavaDataTypeForPK(List<ColumnMetaData> columnMetaDataList);	
}
