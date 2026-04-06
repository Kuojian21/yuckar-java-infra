package com.yuckar.infra.conf.yconfs.context;

import com.yuckar.infra.base.spi.PkgSpiFactory;
import com.yuckar.infra.base.utils.StackUtils;

public class YconfsGroupFactory {

	private static final PkgSpiFactory<YconfsGroupContext> spi = PkgSpiFactory.of(YconfsGroupContext.class);

	public static YconfsGroupContext getContext() {
		return getContext(StackUtils.firstBusinessInvokerClassname());
	}

	public static YconfsGroupContext getContext(Class<?> clazz) {
		return spi.get(clazz);
	}

	public static YconfsGroupContext getContext(String name) {
		return spi.get(name);
	}

}
