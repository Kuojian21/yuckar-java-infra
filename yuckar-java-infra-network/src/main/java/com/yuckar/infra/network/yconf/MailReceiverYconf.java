package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.mail.MailReceiver;
import com.yuckar.infra.network.mail.MailReceiverInfo;

public interface MailReceiverYconf extends CacheableInfoYconf<MailReceiverInfo, MailReceiver> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.mail(key());
	}

}
