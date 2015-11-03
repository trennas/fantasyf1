package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
public class Engine {
	@Id
	private String name;
	private int price;
	private List<Car> cars;

	
	@Override
	public boolean equals(Object otherDriver) {
		if(otherDriver instanceof Engine) {
			if(this.name.equalsIgnoreCase(((Engine) otherDriver).getName())) {
				return true;
			}			
		} 
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
