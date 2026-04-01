package com.yuckar.infra.storage.db.jdbc;

import com.annimon.stream.function.Function;
import com.yuckar.infra.register.resource.IDatabaseResource;

public interface KjdbcRepositoryResource<I> extends KjdbcResource<I>, IDatabaseResource<I, KjdbcHolder> {

	default Function<I, KjdbcHolder> mapper() {
		return config -> KjdbcHolder.of(dataSource(config), tag(config));
	}

	default KjdbcRepository getRepository() {
		return KjdbcRepositoryResourceHolder.getRepository(this);
	}
}
