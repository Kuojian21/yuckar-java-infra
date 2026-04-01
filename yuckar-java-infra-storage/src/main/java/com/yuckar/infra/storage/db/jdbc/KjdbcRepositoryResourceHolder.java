package com.yuckar.infra.storage.db.jdbc;

import java.util.Map;

import com.google.common.collect.Maps;

class KjdbcRepositoryResourceHolder {

	static final Map<KjdbcRepositoryResource<?>, KjdbcRepository> repos = Maps.newConcurrentMap();

	static KjdbcRepository getRepository(KjdbcRepositoryResource<?> resource) {
		return repos.computeIfAbsent(resource, key -> new KjdbcRepository() {
			@Override
			public KjdbcHolder holder(boolean master) {
				return resource.get();
			}
		});
	}

}
