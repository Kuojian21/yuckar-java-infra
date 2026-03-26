package com.yuckar.infra.dlock.context;

import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.yuckar.infra.dlock.DLock;
import com.yuckar.infra.dlock.nolock.NoDLock;

public abstract class AbstractDLockContext implements DLockContext {

	private final ConcurrentMap<String, DLock> locks = Maps.newConcurrentMap();

	@Override
	public final DLock getLock(String key) {
		if (StringUtils.isEmpty(key)) {
			return new NoDLock(key);
		}
		return locks.computeIfAbsent(key, cl -> newLock(key));
	}

	public abstract DLock newLock(String key);

}
