package net.ddns.f1.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
public class Driver {
	@EmbeddedId
	private FullName name;
	private int price;
	private Car car;
	
	@Override
	public boolean equals(Object otherDriver) {
		if(otherDriver instanceof Driver) {
			if(this.getName().equalsIgnoreCase(((Driver) otherDriver).getName())) {
				return true;
			}			
		} 
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	public String getName() {
		return name.getForename() + " " + name.getSurname();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
