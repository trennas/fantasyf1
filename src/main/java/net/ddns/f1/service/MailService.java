package net.ddns.f1.service;

import net.ddns.f1.domain.EventResult;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
	public void sendNewResultsMail(final EventResult result);
}
