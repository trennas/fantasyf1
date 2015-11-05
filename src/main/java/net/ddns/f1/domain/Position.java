package net.ddns.f1.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class Position {
	@Getter @Setter private int position;
	@Getter @Setter private boolean classified;
	
	public Position() {		
	}

	public Position(final int position, final boolean classified) {
		this.position = position;
		this.classified = classified;
	}
}
