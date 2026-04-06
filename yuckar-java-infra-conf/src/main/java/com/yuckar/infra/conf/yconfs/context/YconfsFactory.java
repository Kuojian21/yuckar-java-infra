package com.yuckar.infra.conf.yconfs.context;

import com.yuckar.infra.base.spi.PkgSpiFactory;
import com.yuckar.infra.base.utils.StackUtils;

public class YconfsFactory {

	private static final PkgSpiFactory<YconfsContext> spi = PkgSpiFactory.of(YconfsContext.class);

	public static YconfsContext getContext() {
		return getContext(StackUtils.firstBusinessInvokerClassname());
	}

	public static YconfsContext getContext(Class<?> clazz) {
		return spi.get(clazz);
	}

	public static YconfsContext getContext(String name) {
		return spi.get(name);
	}

}
