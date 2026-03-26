package com.yuckar.infra.storage.legacy;

import com.yuckar.infra.storage.db.jdbc.Kjdbc;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcImpl;

public abstract class KjdbcClusterImpl<T> implements KjdbcCluster<T> {

	private final Class<T> clazz;

	public KjdbcClusterImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Kjdbc<T> sharding(Long key) {
		return new KjdbcImpl<T>(this.clazz) {
			@Override
			public KjdbcHolder holder(boolean master) {
				return KjdbcHolder.of(cluster().getResource(key));
			}
		};
	}

}
