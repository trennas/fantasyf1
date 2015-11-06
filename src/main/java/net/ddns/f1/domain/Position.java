package net.ddns.f1.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class Position implements Comparable<Position> {
	@Getter @Setter private int position;
	@Getter @Setter private boolean classified;
	
	public Position() {		
	}

	public Position(final int position, final boolean classified) {
		this.position = position;
		this.classified = classified;
	}

	@Override
	public int compareTo(Position o) {
		if(this.classified == true && o.classified == false) {
			return -1;
		} else if (this.classified == false && o.classified == true) {
			return -1;
		} else if(this.position < o.position) {
			return -1;
		} else if(this.position == o.position){
			return 0;
		} else {
			return 1;
		}
	}
}
