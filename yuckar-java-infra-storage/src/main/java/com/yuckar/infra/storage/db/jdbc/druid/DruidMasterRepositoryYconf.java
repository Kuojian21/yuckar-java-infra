package com.yuckar.infra.storage.db.jdbc.druid;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterRepositoryYconf;

public interface DruidMasterRepositoryYconf
		extends DruidBaseYconf, MasterRepositoryYconf<Object, DruidMasterRepositoryInfo> {

}
