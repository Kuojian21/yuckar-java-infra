package com.yuckar.infra.storage.jedis;

import com.yuckar.infra.base.executor.LazyExecutor;
import com.yuckar.infra.storage.utils.JedisUtils;

import redis.clients.jedis.JedisSharding;

@SuppressWarnings("deprecation")
public class JedisShardingRepository extends LazyExecutor<JedisSharding, JedisShardingInfo> {

	public JedisShardingRepository(JedisShardingInfo info) {
		super(info, () -> JedisUtils.jedisSharding(info));
	}

}
