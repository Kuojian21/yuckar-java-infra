package com.yuckar.infra.storage.db.jdbc.cluster;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.yuckar.infra.cluster.Master;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;

class MasterClusterRepositoryResourceHolder {

	static final Map<MasterClusterRepositoryResource<?, ?>, ConcurrentMap<Master<KjdbcHolder>, KjdbcRepository>> repos = Maps
			.newConcurrentMap();

	static KjdbcRepository getRepository(MasterClusterRepositoryResource<?, ?> resource, long key) {
		Master<KjdbcHolder> cluster = resource.getResource().getResource(key);
		return repos.computeIfAbsent(resource, k -> new MapMaker().weakKeys().weakValues().makeMap())
				.computeIfAbsent(cluster, sb -> new KjdbcRepository() {
					@Override
					public KjdbcHolder holder(boolean master) {
						return master ? sb.master() : sb.slave();
					}
				});
	}

}
