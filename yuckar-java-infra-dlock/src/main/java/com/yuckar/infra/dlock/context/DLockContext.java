package com.yuckar.infra.dlock.context;

import com.yuckar.infra.common.spi.PkgSpi;
import com.yuckar.infra.dlock.DLock;

public interface DLockContext extends PkgSpi {

	DLock getLock(String key);

	default String pkg() {
		return "";
	}

}
