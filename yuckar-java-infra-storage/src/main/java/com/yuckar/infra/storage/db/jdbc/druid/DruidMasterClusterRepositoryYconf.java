package com.yuckar.infra.storage.db.jdbc.druid;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryYconf;

public interface DruidMasterClusterRepositoryYconf
		extends DruidBaseYconf, MasterClusterRepositoryYconf<Object, DruidMasterClusterRepositoryInfo> {

}
