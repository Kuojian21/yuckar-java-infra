package com.yuckar.infra.dlock.nolock;

import java.util.concurrent.TimeUnit;

import com.yuckar.infra.dlock.AbstractDLock;

public class NoDLock extends AbstractDLock {

	public NoDLock(String key) {
		super(key);
	}

	@Override
	public boolean tryLock(long timeout, TimeUnit unit) {
		logger.debug("tryLock key:{} timeout:{} unit:{}", key(), timeout, unit);
		return true;
	}

	@Override
	public void unlock() {
		logger.debug("unlock key:{}!!!", key());
	}

}
