package com.yuckar.infra.storage.db.jdbc.dbcp2;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryResource;

public interface Dbcp2MasterClusterRepositoryResource
		extends Dbcp2BaseResource, MasterClusterRepositoryResource<Object, Dbcp2MasterClusterRepositoryInfo> {

}
