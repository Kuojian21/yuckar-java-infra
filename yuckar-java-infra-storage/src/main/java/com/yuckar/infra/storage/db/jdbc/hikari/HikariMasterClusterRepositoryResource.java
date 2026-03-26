package com.yuckar.infra.storage.db.jdbc.hikari;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryResource;
import com.zaxxer.hikari.HikariConfig;

public interface HikariMasterClusterRepositoryResource
		extends HikariBaseResource, MasterClusterRepositoryResource<HikariConfig, HikariMasterClusterRepositoryInfo> {

}
