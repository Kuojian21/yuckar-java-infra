package com.yuckar.infra.dlock;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.yuckar.infra.common.logger.LoggerUtils;

public abstract class AbstractDLock implements DLock {

	protected Logger logger = LoggerUtils.logger(getClass());

	private final String key;

	public AbstractDLock(String key) {
		this.key = key;
	}

	@Override
	public void lock() {
		tryLock(-1, null);
	}

	@Override
	public boolean tryLock() {
		return tryLock(1, TimeUnit.MILLISECONDS);
	}

	public String key() {
		return this.key;
	}

}
