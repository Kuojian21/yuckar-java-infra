package com.yuckar.infra.storage.db.jdbc;

import java.util.Map;

import com.google.common.collect.Maps;

class KjdbcRepositoryYconfHolder {

	static final Map<KjdbcRepositoryYconf<?>, KjdbcRepository> repos = Maps.newConcurrentMap();

	static KjdbcRepository getRepository(KjdbcRepositoryYconf<?> yconf) {
		return repos.computeIfAbsent(yconf, key -> new KjdbcRepository() {
			@Override
			public KjdbcHolder holder(boolean master) {
				return yconf.get();
			}
		});
	}

}
