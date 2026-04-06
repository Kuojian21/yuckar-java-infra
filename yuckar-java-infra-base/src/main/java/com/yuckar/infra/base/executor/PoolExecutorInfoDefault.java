package com.yuckar.infra.base.executor;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public abstract class PoolExecutorInfoDefault<T> implements PoolExecutorInfo<T> {

	private GenericObjectPoolConfig<T> poolConfig;

	@Override
	public GenericObjectPoolConfig<T> getPoolConfig() {
		return poolConfig;
	}

	@Override
	public void setPoolConfig(GenericObjectPoolConfig<T> poolConfig) {
		this.poolConfig = poolConfig;
	}

}
