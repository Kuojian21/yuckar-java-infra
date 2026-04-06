package com.yuckar.infra.storage.db.jdbc.cluster;

import com.annimon.stream.function.Function;
import com.yuckar.infra.storage.db.jdbc.KjdbcRepository;
import com.yuckar.infra.storage.db.jdbc.KjdbcYconf;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.yconf.MasterYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.storage.db.jdbc.KjdbcHolder;

public interface MasterRepositoryYconf<I, C extends MasterRepositoryInfo<I>>
		extends KjdbcYconf<I>, MasterYconf<KjdbcHolder, I, C> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.database(key());
	}

	@Override
	default Function<InstanceInfo<I>, KjdbcHolder> mapper() {
		return config -> KjdbcHolder.of(dataSource(config.getInfo()), tag(config.getInfo()));
	}

	default KjdbcRepository getRepository() {
		return MasterRepositoryYconfHolder.getRepository(this);
	}
}
