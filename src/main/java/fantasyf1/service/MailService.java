package fantasyf1.service;

import org.springframework.stereotype.Service;

import fantasyf1.domain.EventResult;

@Service
public interface MailService {
	public void sendNewResultsMail(final EventResult result);
}
