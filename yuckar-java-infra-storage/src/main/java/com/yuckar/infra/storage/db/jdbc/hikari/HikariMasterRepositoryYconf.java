package com.yuckar.infra.storage.db.jdbc.hikari;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterRepositoryYconf;
import com.zaxxer.hikari.HikariConfig;

public interface HikariMasterRepositoryYconf
		extends HikariBaseYconf, MasterRepositoryYconf<HikariConfig, HikariMasterRepositoryInfo> {

}
