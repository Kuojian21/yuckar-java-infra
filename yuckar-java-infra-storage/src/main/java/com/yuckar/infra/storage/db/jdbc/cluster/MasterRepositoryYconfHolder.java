package com.yuckar.infra.storage.db.jdbc.cluster;

import java.util.Map;

import com.google.common.collect.Maps;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;

class MasterRepositoryYconfHolder {

	static final Map<MasterRepositoryYconf<?, ?>, KjdbcRepository> repos = Maps.newConcurrentMap();

	static KjdbcRepository getRepository(MasterRepositoryYconf<?, ?> yconf) {
		return repos.computeIfAbsent(yconf, key -> new KjdbcRepository() {
			@Override
			public KjdbcHolder holder(boolean master) {
				return master ? yconf.getResource().master() : yconf.getResource().slave();
			}
		});
	}

}
