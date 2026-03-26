package com.yuckar.infra.buffer.trigger.impl;

public interface ThresholdBufferTriggerRejectHandler<E> {

	boolean onReject(E element);

}
