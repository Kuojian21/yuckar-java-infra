package com.yuckar.infra.conf.simple;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.ThrowableConsumer;
import com.yuckar.infra.base.hook.HookHelper;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.StackUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;

class SimpleYconfImpl<T, V> implements SimpleYconf<T> {
	private final String key;
	private final Class<V> clazz;
	private final Function<V, T> mapper;
	private final ThrowableConsumer<T, Throwable> release;
	private final LazySupplier<LazySupplier<T>> conf;

	public SimpleYconfImpl(String key, Class<V> clazz, Function<V, T> mapper, ThrowableConsumer<T, Throwable> release) {
		super();
		this.key = key;
		this.clazz = clazz;
		this.mapper = mapper;
		this.release = release;
		String classname = StackUtils.firstBusinessInvokerClassname();
		String path = YconfsNamespaceUtils.common(this.key);
		this.conf = LazySupplier.wrap(() -> {
			Yconfs<V> yconfs = YconfsFactory.getContext(classname).getYconfs(this.clazz);
			yconfs.addListener(path, event -> SimpleYconfImpl.this.refresh());
			if (SimpleYconfImpl.this.release != null) {
				HookHelper.addHook("simple-yconf", SimpleYconfImpl.this::refresh);
			}
			return LazySupplier.wrap(() -> this.mapper.apply(yconfs.get(path)));
		});
	}

//	public String key() {
//		return this.key;
//	}

	@Override
	public T get() {
		return this.conf.get().get();
	}

	public void close() {
		this.refresh();
	}

	private void refresh() {
		this.conf.get().refresh(this.release);
	}
}