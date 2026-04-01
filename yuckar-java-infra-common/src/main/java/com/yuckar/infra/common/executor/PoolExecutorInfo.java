package com.yuckar.infra.common.executor;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public interface PoolExecutorInfo<T> {

	GenericObjectPoolConfig<T> getPoolConfig();

	void setPoolConfig(GenericObjectPoolConfig<T> poolConfig);

}
