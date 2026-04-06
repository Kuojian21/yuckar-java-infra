package com.yuckar.infra.storage.db.jdbc.hikari;

import javax.sql.DataSource;

import com.yuckar.infra.storage.db.jdbc.KjdbcYconf;
import com.yuckar.infra.storage.db.utils.DB_tag_utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public interface HikariBaseYconf extends KjdbcYconf<HikariConfig> {

	@Override
	default DataSource dataSource(HikariConfig info) {
		return new HikariDataSource(info);
	}

	@Override
	default String tag(HikariConfig info) {
		return DB_tag_utils.tag(info.getJdbcUrl());
	}

}
