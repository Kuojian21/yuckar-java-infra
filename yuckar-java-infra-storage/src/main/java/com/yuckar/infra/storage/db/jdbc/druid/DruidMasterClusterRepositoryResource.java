package com.yuckar.infra.storage.db.jdbc.druid;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryResource;

public interface DruidMasterClusterRepositoryResource
		extends DruidBaseResource, MasterClusterRepositoryResource<Object, DruidMasterClusterRepositoryInfo> {

}
