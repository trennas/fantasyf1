package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.ConfigRepository;
import net.ddns.f1.repository.TeamRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl {
	private static final Logger LOG = Logger
			.getLogger(MailServiceImpl.class);
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	TeamRepository teamRepo;
	
	@Autowired
	ConfigRepository configRepo;
	
	@Value("${website-url}")
	private String websiteUrl;
	
	@Value("${new-result-email-alerts}")
	private boolean newResultEmailAlerts;
	
	@Value("${qualifying-result-email-alerts}")
	private boolean qualifyingResultEmailAlerts;
	
	public void sendNewResultsMail(EventResult result) {
		try {
			if(newResultEmailAlerts && (qualifyingResultEmailAlerts || result.isRaceComplete())) {
				if(mailSender instanceof JavaMailSenderImpl) {
					JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
					mailSenderImpl.setPassword(configRepo.findByKey("emailPassword").get(0).getValue());
				}			
				
		        F1MimeMessagePreparator mimePrep = new F1MimeMessagePreparator(result, teamRepo.findAll().iterator());			
				//F1MimeMessagePreparator mimePrep = new F1MimeMessagePreparator(result, teamRepo.findByEmail("mike.trenaman@gmail.com").iterator());			
		        
		        mailSender.send(mimePrep);
		        LOG.info("Sent new" + (result.isRaceComplete() ? " Race " : " Qualifying ") + "results email for " + result.getVenue());
			}
		} catch(Exception e) {
			LOG.error("Error sending mail. " + e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	private class F1MimeMessagePreparator implements MimeMessagePreparator {
		
		private EventResult result;
		private Iterator<Team> itr;
		
    	public F1MimeMessagePreparator(EventResult result, Iterator<Team> itr) {
    		this.result = result;
    		this.itr = itr;
    	}
    	
    	@Override
        public void prepare(MimeMessage mimeMessage) throws Exception {
			List<Address> addresses = new ArrayList<Address>();
			while(itr.hasNext()) {
				addresses.add(new InternetAddress(itr.next().getEmail()));
			}
			
			Address[] addressArray = new Address[addresses.size()];
			
    		mimeMessage.addRecipients(Message.RecipientType.TO, addresses.toArray(addressArray));
        	mimeMessage.setSubject("Fantasy F1 - " + result.getVenue() + (result.isRaceComplete() ? " Race " : " Qualifying ") + "Results");
        	
        	String message = "<font face=\"arial\"><h4>Results From The " + result.getVenue() + "</h4>" +
        					 "<p>" + (result.isRaceComplete() ? "Race " : "Qualifying ")
        					 + "Results from the " + result.getVenue() + " have been received "
        					 + "and scores have been updated.</p>"
        					 + "<p>Visit the Fantasy F1 Webpage to check the latest scores: -</p><br/>"
        					 + "<a href=\"" + websiteUrl + "\">" + websiteUrl + "</a></font>";
        	
        	String htmlMessage = "<!DOCTYPE html><html><head/><body>" + message + "</body></html>";
			mimeMessage.setContent(htmlMessage, "text/html; charset=utf-8");			
        }
    }
}
