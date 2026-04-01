package com.yuckar.infra.storage.db.jdbc.cluster;

import com.annimon.stream.function.Function;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;
import com.yuckar.infra.storage.db.jdbc.KjdbcResource;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.resource.MasterResource;
import com.yuckar.infra.register.utils.RegisterNamespaceUtils;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;

public interface MasterRepositoryResource<I, C extends MasterRepositoryInfo<I>>
		extends KjdbcResource<I>, MasterResource<KjdbcHolder, I, C> {

	String key();

	@Override
	default String path() {
		return RegisterNamespaceUtils.database(key());
	}

	@Override
	default Function<InstanceInfo<I>, KjdbcHolder> mapper() {
		return config -> KjdbcHolder.of(dataSource(config.getInfo()), tag(config.getInfo()));
	}

	default KjdbcRepository getRepository() {
		return MasterRepositoryResourceHolder.getRepository(this);
	}
}
