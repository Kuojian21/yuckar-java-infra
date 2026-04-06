package com.yuckar.infra.conf.yconfs.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.CreateMode;

import com.google.common.collect.Maps;
import com.yuckar.infra.base.json.JsonUtils;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.conf.yconfs.AbstractYconfsGroup;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.YconfsListener;

public class CuratorYconfsGroup<V, I> extends AbstractYconfsGroup<V, I> {

	private final Yconfs<V> v_yconfs;
	private final Yconfs<I> c_yconfs;
	private final Map<String, LazySupplier<CuratorCache>> caches = Maps.newConcurrentMap();
	private final CuratorFramework curator;

	public CuratorYconfsGroup(CuratorFramework curator, Class<V> vclazz, Class<I> iclazz) {
		this.curator = curator;
		this.v_yconfs = new CuratorYconfs<>(curator, vclazz);
		this.c_yconfs = new CuratorYconfs<>(curator, iclazz, key -> {
			String pkey = key.substring(0, key.lastIndexOf("/"));
			return CuratorYconfsGroup.this.cgetData(pkey).clisteners();
		});
	}

	@Override
	protected void init(String path) {
		CuratorCache cache = caches
				.computeIfAbsent(path, k -> LazySupplier.wrap(() -> CuratorCache.build(this.curator, path))).get();
		cache.listenable()
				.addListener(CuratorCacheListener.builder().forCreates(node -> fireCreate(path, node.getPath()))
						.forChanges((oldNode, node) -> logger.info("changes:", node.getPath()))
						.forDeletes(node -> fireRemove(path, node.getPath()))
						.forInitialized(() -> logger.info("initialized")).build());
		cache.start();
	}

	@Override
	protected List<String> keys(String path) {
		return caches.computeIfAbsent(path, k -> LazySupplier.wrap(() -> CuratorCache.build(this.curator, path))).get()
				.stream().filter(node -> !node.getPath().equals(path) || node.getData() != null)
				.map(node -> node.getPath()).toList();
	}

	@Override
	public void cadd(String pkey, I value) {
		try {
			if (this.curator.checkExists().forPath(pkey) == null) {
				this.curator.create().creatingParentsIfNeeded().forPath(pkey);
			}
			String ckey = this.curator.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(pkey,
					JsonUtils.toJson(value).getBytes(StandardCharsets.UTF_8));
			logger.info("ckey:{}", ckey);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public V get(String key) {
		return this.v_yconfs.get(key);
	}

	@Override
	public void set(String key, V value) {
		this.v_yconfs.set(key, value);
	}

	@Override
	public void addListener(String key, YconfsListener<V> listener) {
		this.v_yconfs.addListener(key, listener);
	}

	@Override
	protected Yconfs<I> c_yconfs() {
		return this.c_yconfs;
	}

}
