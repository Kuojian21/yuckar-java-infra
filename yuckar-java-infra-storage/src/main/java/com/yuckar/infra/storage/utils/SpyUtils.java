package com.yuckar.infra.storage.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.annimon.stream.Stream;
import com.yuckar.infra.common.json.ConfigUtils;
import com.yuckar.infra.storage.spy.SpyInfo;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

public class SpyUtils {

	public static MemcachedClient client(SpyInfo info) {
		return client(
				Stream.of(info.getAddrs())
						.map(addr -> new InetSocketAddress((String) addr.get("host"),
								((Integer) addr.get("port")).intValue()))
						.toList(),
				ConfigUtils.config(new ConnectionFactoryBuilder(), info.getConnectionFactory()));
	}

	public static MemcachedClient client(List<InetSocketAddress> addrs,
			ConnectionFactoryBuilder connectionFactoryBuilder) {
		try {
			return new MemcachedClient(connectionFactoryBuilder.build(), addrs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
