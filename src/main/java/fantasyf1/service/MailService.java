package fantasyf1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fantasyf1.domain.EventResult;
import fantasyf1.domain.Team;

@Service
public interface MailService {
	void sendNewResultsMail(final EventResult result, final List<Team> teams);
	void sendEndOfSeasonMail(final List<Team> teams);
}
