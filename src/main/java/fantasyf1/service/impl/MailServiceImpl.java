package fantasyf1.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import fantasyf1.domain.EventResult;
import fantasyf1.domain.Team;
import fantasyf1.service.MailService;
import fantasyf1.service.TeamService;

@Service
public class MailServiceImpl implements MailService {
	private static final Logger LOG = Logger.getLogger(MailServiceImpl.class);
	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TeamService teamService;

	@Value("${website-url}")
	private String websiteUrl;

	@Value("${new-result-email-alerts}")
	private boolean newResultEmailAlerts;

	@Value("${qualifying-result-email-alerts}")
	private boolean qualifyingResultEmailAlerts;
	
	@Value("${new-result-email}")
	private String newResultEmail;
	
	@Value("${end-of-season-email}")
	private String endOfSeasonMessage;

	@Override
	public void sendNewResultsMail(final EventResult result, final List<Team> teams) {
		try {
			if (newResultEmailAlerts
					&& (qualifyingResultEmailAlerts || result.isRaceComplete())) {
				for(int i = 0; i < teams.size(); i++) {
					final Team team = teams.get(i);
					final F1NewResultMimeMessagePreparator mimePrep = new F1NewResultMimeMessagePreparator(i+1,	result, team);
					mailSender.send(mimePrep);
				}				
				LOG.info("Sent new"
						+ (result.isRaceComplete() ? " Race " : " Qualifying ")
						+ "result emails for " + result.getVenue());
			}
		} catch (final Exception e) {
			LOG.error("Error sending new result emails.", e);
		}
	}
	
	@Override
	public void sendEndOfSeasonMail(final List<Team> teams) {
		try {
			if (newResultEmailAlerts) {
				for (int i = 0; i < teams.size(); i++) {
					final Team team = teams.get(i);
					final F1EndOfSeasonMimeMessagePreparator mimePrep = new F1EndOfSeasonMimeMessagePreparator(i+1, team);
					mailSender.send(mimePrep);
				}
				LOG.info("Sent end of season emails.");
			}
		} catch (final Exception e) {
			LOG.error("Error end of season emails", e);
		}
	}

	private class F1NewResultMimeMessagePreparator implements MimeMessagePreparator {

		private final EventResult result;
		private final Team team;
		private final Integer position;

		public F1NewResultMimeMessagePreparator(final Integer position, final EventResult result,
				final Team team) {
			this.result = result;
			this.team = team;
			this.position = position;
		}

		@Override
		public void prepare(final MimeMessage mimeMessage) throws Exception {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(team.getEmail()));
			
			mimeMessage.setSubject("Fantasy F1 - " + result.getVenue()
					+ (result.isRaceComplete() ? " Race " : " Qualifying ")
					+ "Results");

			String message = newResultEmail.replaceAll("#venue#", result.getVenue());
			message = message.replaceAll("#teamname#", team.getName());
			message = message.replaceAll("#position#", Integer.toString(position));
			message = message.replaceAll("#session#", result.isRaceComplete() ? "Race" : "Qualifying");
			message = message.replaceAll("#website#", websiteUrl);
			mimeMessage.setContent(message, "text/html; charset=utf-8");
		}
	}
	
	private class F1EndOfSeasonMimeMessagePreparator implements MimeMessagePreparator {
		private final Team team;
		private final Integer position;

		public F1EndOfSeasonMimeMessagePreparator(final Integer position, final Team team) {
			this.team = team;
			this.position = position;
		}

		@Override
		public void prepare(final MimeMessage mimeMessage) throws Exception {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(team.getEmail()));
			mimeMessage.setSubject("Fantasy F1 - The Season Is Over!");

			String message = endOfSeasonMessage.replaceAll("#winnercongratulations#", position == 1 ? "Congratulations, <b>you have won</b> the fantasy f1 league this year!" : "");
			message = message.replaceAll("#podiumcongratulations#", position == 2 || position == 3 ? "Congratulations for finishing <b>on the podium</b> this year!" : "");
			message = message.replaceAll("#position#", Integer.toString(position));
			message = message.replaceAll("#website#", websiteUrl);
			mimeMessage.setContent(message, "text/html; charset=utf-8");
		}
	}
}
