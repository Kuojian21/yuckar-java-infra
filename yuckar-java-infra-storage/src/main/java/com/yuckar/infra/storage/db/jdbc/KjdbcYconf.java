package com.yuckar.infra.storage.db.jdbc;

import javax.sql.DataSource;

public interface KjdbcYconf<I> {

	DataSource dataSource(I info);

	String tag(I info);

}
