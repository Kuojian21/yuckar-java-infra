package com.yuckar.infra.dlock.context;

import com.yuckar.infra.base.spi.PkgSpi;
import com.yuckar.infra.dlock.DLock;

public interface DLockContext extends PkgSpi {

	DLock getLock(String key);

	default String pkg() {
		return "";
	}

}
