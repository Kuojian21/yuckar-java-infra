package com.yuckar.infra.register.loadingcache;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.common.cache.LoadingCache;
import com.yuckar.infra.common.logger.LoggerUtils;
import com.yuckar.infra.common.utils.ProxyUtils;
import com.yuckar.infra.common.utils.RunUtils;
import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.context.RegisterFactory;
import com.yuckar.infra.register.utils.RegisterNamespaceUtils;

public class KLoadingCacheHelper {

	private static final Logger logger = LoggerUtils.logger(KLoadingCache.class);
	private static final Method refresh = RunUtils
			.catching(() -> KLoadingCache.class.getDeclaredMethod("refresh", new Class<?>[0]));

	@SuppressWarnings("unchecked")
	public static <K, V> KLoadingCache<K, V> wrap(String key, LoadingCache<K, V> cache) {
		if (StringUtils.isEmpty(key)) {
			return ProxyUtils.proxy(KLoadingCache.class, (obj, method, args, proxy) -> {
				if (method.equals(KLoadingCacheHelper.refresh)) {
					return null;
				}
				return method.invoke(cache, args);
			});
		}
		Register<Long> register = RegisterFactory.getContext().getRegister(Long.class);
		String path = RegisterNamespaceUtils.loadingcache(key);
		register.addListener(path, e -> {
			cache.invalidateAll();
		});
		register.get(path);
		return ProxyUtils.proxy(KLoadingCache.class, (obj, method, args, proxy) -> {
			if (method.equals(KLoadingCacheHelper.refresh)) {
				logger.info("key:{} old-value:{}", key, register.get(path));
				register.set(path, System.currentTimeMillis());
				return null;
			}
			return method.invoke(cache, args);
		});
	}

}
