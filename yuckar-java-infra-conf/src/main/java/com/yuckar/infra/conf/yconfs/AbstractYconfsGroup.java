package com.yuckar.infra.conf.yconfs;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.logger.LoggerUtils;

public abstract class AbstractYconfsGroup<V, I> implements YconfsGroup<V, I> {

	protected final Logger logger = LoggerUtils.logger(YconfsGroup.class);

	private final ConcurrentMap<String, Data> datas = Maps.newConcurrentMap();

	@Override
	public final Map<String, I> cget(String pkey) {
		return Stream.of(cgetData(pkey).data.get().get())
				.collect(Collectors.toMap(p -> p, this.c_yconfs()::get, Maps::newLinkedHashMap));
	}

	public final Data cgetData(String pkey) {
		return this.datas.computeIfAbsent(pkey, Data::new);
	}

	@Override
	public final List<String> ckeys(String pkey) {
		return cgetData(pkey).data.get().get();
	}

	@Override
	public void cset(String ckey, I value) {
		this.c_yconfs().set(ckey, value);
	}

	@Override
	public void caddListener(String pkey, YconfsGroupListener listener) {
		logger.debug("add listener for [{}]'s gourp!!!", pkey);
		cgetData(pkey).plisteners.add(listener);
	}

	@Override
	public void caddListener(String pkey, YconfsListener<I> listener) {
		logger.debug("add listener for [{}]'s children!!!", pkey);
		Data data = cgetData(pkey);
		data.clisteners.add(listener);
	}

	protected void fireCreate(String pkey, String ckey) {
		logger.info("fireCreate,pkey:[{}] ckey:[{}]!!!", pkey, ckey);
		Data data = cgetData(pkey);
//		try {
//			data.write.lock();
		data.data.refresh();
		data.plisteners.forEach(listener -> {
			listener.onCreate(ckey);
		});
//		} finally {
//			data.write.unlock();
//		}
	}

	protected void fireRemove(String pkey, String ckey) {
		logger.info("fireRemove,pkey:[{}] ckey:[{}]!!!", pkey, ckey);
		Data data = cgetData(pkey);
//		try {
//			data.write.lock();
		data.data.refresh();
		data.plisteners.forEach(listener -> {
			listener.onRemove(ckey);
		});
//		} finally {
//			data.write.unlock();
//		}
	}

	protected abstract void init(String path);

	protected abstract List<String> keys(String path);

	protected abstract Yconfs<I> c_yconfs();

	protected class Data {

		final Set<YconfsGroupListener> plisteners = Sets.newConcurrentHashSet();
		final Set<YconfsListener<I>> clisteners = Sets.newConcurrentHashSet();
//		final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//		final Lock read = lock.readLock();
//		final Lock write = lock.writeLock();
		final String path;
		final LazySupplier<LazySupplier<List<String>>> data;

		Data(String path) {
			super();
			this.path = path;
			this.data = LazySupplier.wrap(() -> {
				logger.info("The group path:[{}] is initing!!!", path);
				init(path);
				return LazySupplier.wrap(() -> {
					return keys(path);
				});
			});
		}

		public Set<YconfsListener<I>> clisteners() {
			return this.clisteners;
		}
	}

}
