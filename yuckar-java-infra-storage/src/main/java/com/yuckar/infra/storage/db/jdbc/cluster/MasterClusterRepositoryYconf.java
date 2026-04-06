package com.yuckar.infra.storage.db.jdbc.cluster;

import com.annimon.stream.function.Function;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.yconf.MasterClusterYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;
import com.yuckar.infra.storage.db.jdbc.KjdbcYconf;

public interface MasterClusterRepositoryYconf<I, C extends MasterClusterRepositoryInfo<I>>
		extends KjdbcYconf<I>, MasterClusterYconf<KjdbcHolder, I, C> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.database(key());
	}

	@Override
	default Function<InstanceInfo<I>, KjdbcHolder> mapper() {
		return config -> KjdbcHolder.of(dataSource(config.getInfo()), tag(config.getInfo()));
	}

	default KjdbcRepository getRepository(long key) {
		return MasterClusterRepositoryYconfHolder.getRepository(this, key);
	}

}
