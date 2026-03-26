package com.yuckar.infra.register.context;

import com.yuckar.infra.common.spi.PkgSpi;
import com.yuckar.infra.register.Register;

public interface RegisterContext extends PkgSpi {

	<I> Register<I> getRegister(Class<I> clazz);

}
