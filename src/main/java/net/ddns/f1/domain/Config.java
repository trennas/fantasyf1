package net.ddns.f1.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Config {
	@Id
	private String key;
	private String value;
	
	public Config() {		
	}
	
	public Config(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
