package com.yuckar.infra.base.buffer.trigger;

public interface ThresholdBufferTriggerRejectHandler<E> {

	boolean onReject(E element);

}
