package com.yuckar.infra.register;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yuckar.infra.common.json.ConfigUtils;
import com.yuckar.infra.common.lazy.LazySupplier;
import com.yuckar.infra.common.logger.LoggerUtils;

public abstract class AbstractRegister<V> implements Register<V> {

	protected final Logger logger = LoggerUtils.logger(Register.class);

	private final ConcurrentMap<String, Data> datas = Maps.newConcurrentMap();
	private final Class<V> clazz;
	private final Function<String, Set<RegisterListener<V>>> listeners;

	public AbstractRegister(Class<V> clazz, Function<String, Set<RegisterListener<V>>> listeners) {
		this.clazz = clazz;
		this.listeners = listeners;
	}

	@Override
	public final V get(String path) {
		return getData(path).data.get().get();
	}

	private Data getData(String path) {
		return datas.computeIfAbsent(path, Data::new);
	}

	@Override
	public final void addListener(String path, RegisterListener<V> listener) {
		logger.debug("add listener for [{}]!!!", path);
		getData(path).listeners.add(listener);
	}

	protected void refresh(String path) {
		V oData = this.get(path);
		datas.get(path).data.get().refresh();
		V nData = this.get(path);

		RegisterEvent<V> event = new RegisterEvent<V>();
		event.setPath(path);

		if (Objects.equal(oData, nData)) {

		} else {
			logger.info("fireChange path:[{}]!!!", event.getPath());
			this.getData(event.getPath()).listeners.forEach(listerner -> listerner.onChange(event));
			Optional.ofNullable(listeners).map(f -> f.apply(path)).ifPresent(ls -> ls.forEach(l -> l.onChange(event)));
		}
	}

//	protected String defString() {
//		if (this.clazz.isPrimitive() || Number.class.isAssignableFrom(this.clazz) || this.clazz == Character.class
//				|| this.clazz == Boolean.class || this.clazz == String.class) {
//			return null;
//		} else if (this.clazz.isArray() || List.class.isAssignableFrom(this.clazz)
//				|| Set.class.isAssignableFrom(this.clazz)) {
//			return "[]";
//		}
//		return "{}";
//	}

	protected abstract void init(String path);

	protected abstract Object json(String path);

	class Data {
		final Set<RegisterListener<V>> listeners = Sets.newConcurrentHashSet();
		final String path;
		final LazySupplier<LazySupplier<V>> data;

		Data(String path) {
			super();
			this.path = path;
			this.data = LazySupplier.wrap(() -> {
				logger.info("The path:[{}] is initing!!!", path);
				init(path);
				return LazySupplier.wrap(() -> {
					return ConfigUtils.valueUnchecked(json(path), clazz);
				});
			});
		}
	}

}
