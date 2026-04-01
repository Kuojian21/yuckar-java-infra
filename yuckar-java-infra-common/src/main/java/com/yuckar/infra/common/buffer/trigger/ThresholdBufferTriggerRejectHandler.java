package com.yuckar.infra.common.buffer.trigger;

public interface ThresholdBufferTriggerRejectHandler<E> {

	boolean onReject(E element);

}
