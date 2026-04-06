package com.yuckar.infra.storage.spy;

import com.yuckar.infra.base.executor.LazyExecutor;
import com.yuckar.infra.storage.utils.SpyUtils;

import net.spy.memcached.MemcachedClient;

public class SpyRepository extends LazyExecutor<MemcachedClient, SpyInfo> {

	public SpyRepository(SpyInfo info) {
		super(info, () -> SpyUtils.client(info));
	}

}
