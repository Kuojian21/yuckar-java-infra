package com.yuckar.infra.storage.db.jdbc;

import com.annimon.stream.function.Function;
import com.yuckar.infra.conf.info.DatabaseYconf;

public interface KjdbcRepositoryYconf<I> extends KjdbcYconf<I>, DatabaseYconf<I, KjdbcHolder> {

	default Function<I, KjdbcHolder> mapper() {
		return config -> KjdbcHolder.of(dataSource(config), tag(config));
	}

	default KjdbcRepository getRepository() {
		return KjdbcRepositoryYconfHolder.getRepository(this);
	}
}
