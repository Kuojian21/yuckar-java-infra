package com.yuckar.infra.register.group.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.CreateMode;

import com.google.common.collect.Maps;
import com.yuckar.infra.common.json.JsonUtils;
import com.yuckar.infra.common.lazy.LazySupplier;
import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.RegisterListener;
import com.yuckar.infra.register.group.AbstractGroupReigster;
import com.yuckar.infra.register.impl.CuratorRegister;

public class CuratorGroupRegister<V, I> extends AbstractGroupReigster<V, I> {

	private final Register<V> vregister;
	private final Register<I> cregister;
	private final Map<String, LazySupplier<CuratorCache>> caches = Maps.newConcurrentMap();
	private final CuratorFramework curator;

	public CuratorGroupRegister(CuratorFramework curator, Class<V> vclazz, Class<I> iclazz) {
		this.curator = curator;
		this.vregister = new CuratorRegister<>(curator, vclazz);
		this.cregister = new CuratorRegister<>(curator, iclazz, key -> {
			String pkey = key.substring(0, key.lastIndexOf("/"));
			return CuratorGroupRegister.this.cgetData(pkey).clisteners();
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
		return this.vregister.get(key);
	}

	@Override
	public void set(String key, V value) {
		this.vregister.set(key, value);
	}

	@Override
	public void addListener(String key, RegisterListener<V> listener) {
		this.vregister.addListener(key, listener);
	}

	@Override
	protected Register<I> cregister() {
		return this.cregister;
	}

}
