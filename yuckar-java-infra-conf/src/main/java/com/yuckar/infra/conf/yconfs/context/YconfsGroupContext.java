package com.yuckar.infra.conf.yconfs.context;

import com.yuckar.infra.base.spi.PkgSpi;
import com.yuckar.infra.conf.yconfs.YconfsGroup;

public interface YconfsGroupContext extends PkgSpi {

	<V, I> YconfsGroup<V, I> getYconfsGroup(Class<V> vclass, Class<I> clazz);

}
