package net.ddns.f1.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class EmailConfig {
	@Id
	private String password;
	
	public EmailConfig() {		
	}
	
	public EmailConfig(String password) {
		this.password = password;
	}
}
