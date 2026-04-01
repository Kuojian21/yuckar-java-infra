package com.yuckar.infra.storage.db.jdbc.cluster;

import com.annimon.stream.function.Function;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.resource.MasterClusterResource;
import com.yuckar.infra.register.utils.RegisterNamespaceUtils;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;
import com.yuckar.infra.storage.db.jdbc.KjdbcResource;

public interface MasterClusterRepositoryResource<I, C extends MasterClusterRepositoryInfo<I>>
		extends KjdbcResource<I>, MasterClusterResource<KjdbcHolder, I, C> {

	String key();

	@Override
	default String path() {
		return RegisterNamespaceUtils.database(key());
	}

	@Override
	default Function<InstanceInfo<I>, KjdbcHolder> mapper() {
		return config -> KjdbcHolder.of(dataSource(config.getInfo()), tag(config.getInfo()));
	}

	default KjdbcRepository getRepository(long key) {
		return MasterClusterRepositoryResourceHolder.getRepository(this, key);
	}

}
