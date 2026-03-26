package com.yuckar.infra.storage.db.jdbc.druid;

import com.yuckar.infra.storage.db.jdbc.cluster.MasterRepositoryResource;

public interface DruidMasterRepositoryResource
		extends DruidBaseResource, MasterRepositoryResource<Object, DruidMasterRepositoryInfo> {

}
