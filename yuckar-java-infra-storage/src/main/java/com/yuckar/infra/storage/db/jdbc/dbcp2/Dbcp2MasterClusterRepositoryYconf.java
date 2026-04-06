package com.yuckar.infra.storage.db.jdbc.dbcp2;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryYconf;

public interface Dbcp2MasterClusterRepositoryYconf
		extends Dbcp2BaseYconf, MasterClusterRepositoryYconf<Object, Dbcp2MasterClusterRepositoryInfo> {

}
