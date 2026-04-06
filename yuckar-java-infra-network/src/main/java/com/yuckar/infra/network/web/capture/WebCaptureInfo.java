package com.yuckar.infra.network.web.capture;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;

import com.yuckar.infra.base.executor.PoolExecutorInfo;

public class WebCaptureInfo<T extends WebDriver> implements PoolExecutorInfo<T> {

	private GenericObjectPoolConfig<T> poolConfig;

	@Override
	public GenericObjectPoolConfig<T> getPoolConfig() {
		return this.poolConfig;
	}

	@Override
	public void setPoolConfig(GenericObjectPoolConfig<T> poolConfig) {
		this.poolConfig = poolConfig;
	}
}
