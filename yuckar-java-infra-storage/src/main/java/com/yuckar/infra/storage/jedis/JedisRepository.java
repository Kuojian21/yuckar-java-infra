package com.yuckar.infra.storage.jedis;

import com.yuckar.infra.common.executor.LazyExecutor;
import com.yuckar.infra.storage.utils.JedisUtils;

import redis.clients.jedis.JedisPool;

public class JedisRepository extends LazyExecutor<JedisPool, JedisInfo> {

	public JedisRepository(JedisInfo info) {
		super(info, () -> JedisUtils.jedis(info));
	}

}
