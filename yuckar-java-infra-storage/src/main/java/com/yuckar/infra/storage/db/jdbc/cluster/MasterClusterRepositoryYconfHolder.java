package com.yuckar.infra.storage.db.jdbc.cluster;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.yuckar.infra.cluster.Master;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;

class MasterClusterRepositoryYconfHolder {

	static final Map<MasterClusterRepositoryYconf<?, ?>, ConcurrentMap<Master<KjdbcHolder>, KjdbcRepository>> repos = Maps
			.newConcurrentMap();

	static KjdbcRepository getRepository(MasterClusterRepositoryYconf<?, ?> yconf, long key) {
		Master<KjdbcHolder> cluster = yconf.getResource().getResource(key);
		return repos.computeIfAbsent(yconf, k -> new MapMaker().weakKeys().weakValues().makeMap())
				.computeIfAbsent(cluster, sb -> new KjdbcRepository() {
					@Override
					public KjdbcHolder holder(boolean master) {
						return master ? sb.master() : sb.slave();
					}
				});
	}

}
