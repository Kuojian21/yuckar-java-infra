package com.yuckar.infra.storage.legacy;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.storage.db.jdbc.Kjdbc;

public interface KjdbcCluster<T> {

	Kjdbc<T> sharding(Long key);

	Cluster<NamedParameterJdbcTemplate> cluster();

}
