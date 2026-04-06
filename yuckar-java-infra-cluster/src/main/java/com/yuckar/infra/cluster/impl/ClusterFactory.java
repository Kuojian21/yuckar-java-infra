package com.yuckar.infra.cluster.impl;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.ThrowableConsumer;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.cluster.info.ClusterInfo;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.utils.InfoObjectEquals;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.YconfsEvent;
import com.yuckar.infra.conf.yconfs.YconfsGroup;
import com.yuckar.infra.conf.yconfs.YconfsGroupListener;
import com.yuckar.infra.conf.yconfs.YconfsListener;

public class ClusterFactory {

	public static <R, I, C extends ClusterInfo<I>> Cluster<R> cluster(YconfsGroup<C, I> yconfsg, String key,
			Function<InstanceInfo<I>, R> mapper, ThrowableConsumer<R, Exception> release) {
		LazySupplier<C> info = LazySupplier.wrap(() -> {
			C cinfo = yconfsg.get(key);
			cinfo.setInstanceInfos(
					Stream.of(yconfsg.cget(key)).map(p -> InstanceInfo.of(p.getKey(), p.getValue())).toList());
			return cinfo;
		});
		LazySupplier<ClusterImpl<R, I, C>> cluster = LazySupplier
				.wrap(() -> new ClusterImpl<R, I, C>(info, mapper, release));
		final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		final Lock read = lock.readLock();
		final Lock write = lock.writeLock();
		yconfsg.addListener(key, new YconfsListener<>() {
			@Override
			public void onChange(YconfsEvent<C> event) {
				try {
					read.lock();
					info.refresh();
					cluster.get().refresh();
				} finally {
					read.unlock();
				}
			}
		});
		yconfsg.caddListener(key, new YconfsGroupListener() {

			@Override
			public void onCreate(String ckey) {
				try {
					read.lock();
					info.refresh();
					cluster.get().add(ckey);
				} finally {
					read.unlock();
				}
			}

			@Override
			public void onRemove(String ckey) {
				try {
					read.lock();
					info.refresh();
					cluster.get().remove(ckey);
				} finally {
					read.unlock();
				}
			}

		});
		yconfsg.caddListener(key, new YconfsListener<>() {

			@Override
			public void onChange(YconfsEvent<I> event) {
				try {
					read.lock();
					info.refresh();
					cluster.get().refresh(event.getPath());
				} finally {
					read.unlock();
				}
			}

		});
		try {
			write.lock();
			return cluster.get();
		} finally {
			write.unlock();
		}
	}

	public static <R, I, C extends ClusterInfo<I>> Cluster<R> cluster(Yconfs<C> yconfs, String key,
			Function<InstanceInfo<I>, R> mapper, ThrowableConsumer<R, Exception> release) {
		LazySupplier<C> info = LazySupplier.wrap(() -> yconfs.get(key));
		LazySupplier<ClusterImpl<R, I, C>> cluster = LazySupplier
				.wrap(() -> new ClusterImpl<R, I, C>(info, mapper, release));
		Object lock = new Object();
		yconfs.addListener(key, new YconfsListener<>() {
			@Override
			public void onChange(YconfsEvent<C> event) {
				synchronized (lock) {
					ClusterInfo<?> oData = (ClusterInfo<?>) info.get();
					info.refresh();
					cluster.get().refresh();
					ClusterInfo<?> nData = (ClusterInfo<?>) info.get();
					Map<String, ?> oMap = Stream.of(oData.getInstanceInfos())
							.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
					Map<String, ?> nMap = Stream.of(nData.getInstanceInfos())
							.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
					oMap.forEach((name, iInfo) -> {
						if (!nMap.containsKey(name)) {
							cluster.get().remove(name);
						} else {
							if (InfoObjectEquals.equals(iInfo, nMap.get(name))) {

							} else {
								cluster.get().refresh(name);
							}
							nMap.remove(name);
						}
					});
					nMap.forEach((name, iInfo) -> {
						cluster.get().add(key);
					});
				}
			}
		});
		synchronized (lock) {
			return cluster.get();
		}
	}

}
