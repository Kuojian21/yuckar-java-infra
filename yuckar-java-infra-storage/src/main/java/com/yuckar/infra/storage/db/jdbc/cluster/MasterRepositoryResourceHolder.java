package com.yuckar.infra.storage.db.jdbc.cluster;

import java.util.Map;

import com.google.common.collect.Maps;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;

class MasterRepositoryResourceHolder {

	static final Map<MasterRepositoryResource<?, ?>, KjdbcRepository> repos = Maps.newConcurrentMap();

	static KjdbcRepository getRepository(MasterRepositoryResource<?, ?> resource) {
		return repos.computeIfAbsent(resource, key -> new KjdbcRepository() {
			@Override
			public KjdbcHolder holder(boolean master) {
				return master ? resource.getResource().master() : resource.getResource().slave();
			}
		});
	}

}
