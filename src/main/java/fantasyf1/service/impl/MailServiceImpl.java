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

	@Override
	public void sendNewResultsMail(final EventResult result) {
		try {
			if (newResultEmailAlerts
					&& (qualifyingResultEmailAlerts || result.isRaceComplete())) {
				final F1MimeMessagePreparator mimePrep = new F1MimeMessagePreparator(
						result, teamService.findAll());
				// F1MimeMessagePreparator mimePrep = new
				// F1MimeMessagePreparator(result,
				// teamRepo.findByEmail("mike.trenaman@gmail.com").iterator());

				mailSender.send(mimePrep);
				LOG.info("Sent new"
						+ (result.isRaceComplete() ? " Race " : " Qualifying ")
						+ "results email for " + result.getVenue());
			}
		} catch (final Exception e) {
			LOG.error("Error sending mail. " + e.getClass().getName() + ": "
					+ e.getMessage());
		}
	}

	private class F1MimeMessagePreparator implements MimeMessagePreparator {

		private final EventResult result;
		private final List<Team> teams;

		public F1MimeMessagePreparator(final EventResult result,
				final List<Team> teams) {
			this.result = result;
			this.teams = teams;
		}

		@Override
		public void prepare(final MimeMessage mimeMessage) throws Exception {
			final List<Address> addresses = new ArrayList<>();
			for (final Team team : teams) {
				addresses.add(new InternetAddress(team.getEmail()));
			}

			final Address[] addressArray = new Address[addresses.size()];

			mimeMessage.addRecipients(Message.RecipientType.BCC,
					addresses.toArray(addressArray));
			mimeMessage.setSubject("Fantasy F1 - " + result.getVenue()
					+ (result.isRaceComplete() ? " Race " : " Qualifying ")
					+ "Results");

			final String message = "<font face=\"arial\"><h4>Results From The "
					+ result.getVenue()
					+ "</h4>"
					+ "<p>"
					+ (result.isRaceComplete() ? "Race " : "Qualifying ")
					+ "Results from the "
					+ result.getVenue()
					+ " have been received "
					+ "and scores have been updated.</p>"
					+ "<p>Visit the Fantasy F1 Webpage to check the latest scores: -</p><br/>"
					+ "<a href=\"" + websiteUrl + "\">" + websiteUrl
					+ "</a></font>";

			final String htmlMessage = "<!DOCTYPE html><html><head/><body>"
					+ message + "</body></html>";
			mimeMessage.setContent(htmlMessage, "text/html; charset=utf-8");
		}
	}
}
