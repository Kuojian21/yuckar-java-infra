package com.yuckar.infra.dlock.nolock;

import com.yuckar.infra.dlock.DLock;
import com.yuckar.infra.dlock.context.AbstractDLockContext;
import com.yuckar.infra.dlock.context.DLockContext;

public class NoDLockContext extends AbstractDLockContext implements DLockContext {

	@Override
	public DLock newLock(String key) {
		return new NoDLock(key);
	}

	@Override
	public String pkg() {
		return "";
	}

}
