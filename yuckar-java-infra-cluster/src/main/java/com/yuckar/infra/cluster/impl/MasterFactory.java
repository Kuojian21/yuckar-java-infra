package com.yuckar.infra.cluster.impl;

import java.util.Map;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.ThrowableConsumer;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.cluster.Master;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.info.MasterInfo;
import com.yuckar.infra.cluster.utils.InfoObjectEquals;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.YconfsEvent;
import com.yuckar.infra.conf.yconfs.YconfsListener;

public class MasterFactory {

	public static <R, I, M extends MasterInfo<I>> Master<R> master(Yconfs<M> yconfs, String key,
			Function<InstanceInfo<I>, R> mapper, ThrowableConsumer<R, Exception> release) {
		LazySupplier<M> info = LazySupplier.wrap(() -> yconfs.get(key));
		LazySupplier<MasterImpl<R, I, M>> master = LazySupplier
				.wrap(() -> new MasterImpl<R, I, M>(info, mapper, release));

		Object lock = new Object();
		yconfs.addListener(key, new YconfsListener<>() {
			@Override
			public void onChange(YconfsEvent<M> event) {
				synchronized (lock) {
					MasterInfo<?> oData = (MasterInfo<?>) info.get();
					info.refresh();
					MasterInfo<?> nData = (MasterInfo<?>) info.get();

					Object oMasterInfo = oData.getMaster();
					Object nMasterInfo = nData.getMaster();
					if (!InfoObjectEquals.equals(oMasterInfo, nMasterInfo)) {
						master.get().refresh();
					}

					Map<String, ?> oMap = Stream.of(oData.getSlaves().getInstanceInfos())
							.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
					Map<String, ?> nMap = Stream.of(nData.getSlaves().getInstanceInfos())
							.collect(Collectors.toMap(i -> i.getName(), i -> i.getInfo()));
					oMap.forEach((name, iInfo) -> {
						if (!nMap.containsKey(name)) {
							master.get().remove(name);
						} else {
							if (InfoObjectEquals.equals(iInfo, nMap.get(name))) {

							} else {
								master.get().refresh(name);
							}
							nMap.remove(name);
						}
					});
					nMap.forEach((name, iInfo) -> {
						master.get().add(key);
					});
				}
			}
		});
		synchronized (lock) {
			return master.get();
		}
	}

}
