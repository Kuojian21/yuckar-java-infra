package com.yuckar.infra.register.group.context;

import com.yuckar.infra.common.spi.PkgSpiFactory;
import com.yuckar.infra.common.utils.StackUtils;

public class GroupRegisterFactory {

	private static final PkgSpiFactory<GroupRegisterContext> spi = PkgSpiFactory.of(GroupRegisterContext.class);

	public static GroupRegisterContext getContext() {
		return getContext(StackUtils.firstBusinessInvokerClassname());
	}

	public static GroupRegisterContext getContext(Class<?> clazz) {
		return spi.get(clazz);
	}

	public static GroupRegisterContext getContext(String name) {
		return spi.get(name);
	}

}
