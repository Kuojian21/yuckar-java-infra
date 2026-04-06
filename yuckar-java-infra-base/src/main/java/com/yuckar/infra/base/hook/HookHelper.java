package com.yuckar.infra.base.hook;

import java.util.Map;

import com.annimon.stream.Optional;
import com.google.common.collect.Maps;
import com.yuckar.infra.base.function.ThrowableRunnable;
import com.yuckar.infra.base.lazy.LazySupplier;

public class HookHelper {

	private static final Map<String, LazySupplier<Hooks>> hooks = Maps.newConcurrentMap();

	public static void addHook(String module, ThrowableRunnable<? extends Throwable> hook) {
		hooks.computeIfAbsent(Optional.ofNullable(module).orElse("def"), k -> LazySupplier.wrap(() -> new Hooks(k)))
				.get().add(hook);
	}

}
