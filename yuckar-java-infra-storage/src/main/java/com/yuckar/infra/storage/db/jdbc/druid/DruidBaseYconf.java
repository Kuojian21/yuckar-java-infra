package com.yuckar.infra.storage.db.jdbc.druid;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuckar.infra.base.json.ConfigUtils;
import com.yuckar.infra.storage.db.jdbc.KjdbcYconf;
import com.yuckar.infra.storage.db.utils.DB_tag_utils;

@SuppressWarnings("unchecked")
public interface DruidBaseYconf extends KjdbcYconf<Object> {

	@Override
	default DataSource dataSource(Object obj) {
		try {
			Map<String, Object> config = (Map<String, Object>) obj;
			DruidDataSource dataSource = new DruidDataSource();
			ConfigUtils.config(dataSource, config);
			dataSource.init();
			return dataSource;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	default String tag(Object obj) {
		Map<String, Object> config = (Map<String, Object>) obj;
		return DB_tag_utils.tag(config.get("url").toString());
	}
}
