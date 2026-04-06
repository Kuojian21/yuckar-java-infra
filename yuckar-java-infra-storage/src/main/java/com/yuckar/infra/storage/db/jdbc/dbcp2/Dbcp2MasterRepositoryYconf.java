package com.yuckar.infra.storage.db.jdbc.dbcp2;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterRepositoryYconf;

public interface Dbcp2MasterRepositoryYconf
		extends Dbcp2BaseYconf, MasterRepositoryYconf<Object, Dbcp2MasterRepositoryInfo> {

}
