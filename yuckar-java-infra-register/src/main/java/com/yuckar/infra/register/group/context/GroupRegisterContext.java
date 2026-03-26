package com.yuckar.infra.register.group.context;

import com.yuckar.infra.common.spi.PkgSpi;
import com.yuckar.infra.register.group.GroupRegister;

public interface GroupRegisterContext extends PkgSpi {

	<V, I> GroupRegister<V, I> getGroupRegister(Class<V> vclass, Class<I> clazz);

}
