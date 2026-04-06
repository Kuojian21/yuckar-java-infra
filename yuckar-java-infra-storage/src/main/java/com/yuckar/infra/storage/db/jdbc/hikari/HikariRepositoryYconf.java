package com.yuckar.infra.storage.db.jdbc.hikari;

import com.yuckar.infra.storage.db.jdbc.KjdbcRepositoryYconf;
import com.zaxxer.hikari.HikariConfig;

public interface HikariRepositoryYconf extends HikariBaseYconf, KjdbcRepositoryYconf<HikariConfig> {

}