package com.yuckar.infra.conf.yconfs.context;

import com.yuckar.infra.base.spi.PkgSpi;
import com.yuckar.infra.conf.yconfs.Yconfs;

public interface YconfsContext extends PkgSpi {

	<I> Yconfs<I> getYconfs(Class<I> clazz);

}
