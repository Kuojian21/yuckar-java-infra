package com.yuckar.infra.conf.loadingcache;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.common.cache.LoadingCache;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.utils.ProxyUtils;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;

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
		Yconfs<Long> yconfs = YconfsFactory.getContext().getYconfs(Long.class);
		String path = YconfsNamespaceUtils.loadingcache(key);
		yconfs.addListener(path, e -> {
			cache.invalidateAll();
		});
		yconfs.get(path);
		return ProxyUtils.proxy(KLoadingCache.class, (obj, method, args, proxy) -> {
			if (method.equals(KLoadingCacheHelper.refresh)) {
				logger.info("key:{} old-value:{}", key, yconfs.get(path));
				yconfs.set(path, System.currentTimeMillis());
				return null;
			}
			return method.invoke(cache, args);
		});
	}

}
