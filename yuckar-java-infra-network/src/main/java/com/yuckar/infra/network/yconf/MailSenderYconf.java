package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.mail.MailSender;
import com.yuckar.infra.network.mail.MailSenderInfo;

public interface MailSenderYconf extends CacheableInfoYconf<MailSenderInfo, MailSender> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.mail(key());
	}

}
