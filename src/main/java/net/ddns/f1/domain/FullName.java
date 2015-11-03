package net.ddns.f1.domain;

import lombok.Data;

@Data
public class FullName {
	public String forename;
	public String surname;
	
	public FullName() {	
	}
	
	public FullName(String fullNameSpaceSeparated) {
		String[] names = fullNameSpaceSeparated.split(" ");
		this.forename = names[0];
		this.surname = names[1];
	}
	
	public FullName(String forename, String surname) {
		this.forename = forename;
		this.surname = surname;
	}
}
