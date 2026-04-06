package com.yuckar.infra.base.utils;

import java.util.concurrent.locks.Lock;

import com.annimon.stream.function.ThrowableSupplier;
import com.yuckar.infra.base.function.ThrowableRunnable;

public class LockUtils {

	public static <T, X extends Throwable> void runInLock(Lock lock, ThrowableRunnable<X> runnable) throws X {
		runInLock(lock, () -> {
			runnable.run();
			return null;
		});
	}

	public static <T, X extends Throwable> T runInLock(Lock lock, ThrowableSupplier<T, X> supplier) throws X {
		lock.lock();
		try {
			return supplier.get();
		} finally {
			lock.unlock();
		}
	}

}
