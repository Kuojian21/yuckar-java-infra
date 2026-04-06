package com.yuckar.infra.storage.db.jdbc.hikari;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryYconf;
import com.zaxxer.hikari.HikariConfig;

public interface HikariMasterClusterRepositoryYconf
		extends HikariBaseYconf, MasterClusterRepositoryYconf<HikariConfig, HikariMasterClusterRepositoryInfo> {

}
