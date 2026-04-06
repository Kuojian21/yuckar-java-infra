package com.yuckar.infra.base.term;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class SignalHelper {

	static void handle(String signal, SignalHandler handler) {
		Signal.handle(new Signal(signal), handler);
	}

	public static void raise(Signal signal) {
		Signal.raise(signal);
	}
}
