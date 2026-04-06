package com.yuckar.infra.cluster.impl;

import java.util.Map;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.ThrowableConsumer;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.cluster.MasterCluster;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.info.MasterClusterInfo;
import com.yuckar.infra.cluster.info.MasterInfo;
import com.yuckar.infra.cluster.utils.InfoObjectEquals;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.YconfsEvent;
import com.yuckar.infra.conf.yconfs.YconfsListener;

public class MasterClusterFactory {

	public static <R, I, C extends MasterClusterInfo<I>> MasterCluster<R> cluster(Yconfs<C> yconfs, String key,
			Function<InstanceInfo<I>, R> mapper, ThrowableConsumer<R, Exception> release) {
		LazySupplier<C> info = LazySupplier.wrap(() -> yconfs.get(key));
		LazySupplier<MasterClusterImpl<R, I, C>> cluster = LazySupplier
				.wrap(() -> new MasterClusterImpl<R, I, C>(info, mapper, release));
		Object lock = new Object();
		yconfs.addListener(key, new YconfsListener<C>() {
			@Override
			public void onChange(YconfsEvent<C> event) {
				synchronized (lock) {
					MasterClusterInfo<?> oData = (MasterClusterInfo<?>) info.get();
					info.refresh();
					MasterClusterInfo<?> nData = (MasterClusterInfo<?>) info.get();
					Map<String, MasterInfo<?>> oMap = Stream.of(oData.getInstanceInfos())
							.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
					Map<String, MasterInfo<?>> nMap = Stream.of(nData.getInstanceInfos())
							.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));

					oMap.forEach((name, oInfo) -> {
						if (!nMap.containsKey(name)) {
							cluster.get().remove(name);
						} else {
							MasterInfo<?> nInfo = nMap.get(name);
							if (InfoObjectEquals.equals(oInfo, nInfo)) {

							} else {
								Object om = oInfo.getMaster();
								Object nm = nInfo.getMaster();
								if (InfoObjectEquals.equals(om, nm)) {

								} else {
									cluster.get().refresh(name);
								}
								Map<String, ?> osMap = Stream.of(oInfo.getSlaves().getInstanceInfos())
										.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
								Map<String, ?> nsMap = Stream.of(nInfo.getSlaves().getInstanceInfos())
										.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
								osMap.forEach((sname, soinfo) -> {
									if (!nsMap.containsKey(sname)) {
										cluster.get().remove(name, sname);
									} else {
										Object sninfo = nsMap.get(sname);
										if (InfoObjectEquals.equals(soinfo, sninfo)) {

										} else {
											cluster.get().refresh(name, sname);
										}
										nsMap.remove(sname);
									}
								});
								nsMap.forEach((sname, sninfo) -> {
									cluster.get().add(name, sname);
								});
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
