package net.ddns.f1.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Correction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int round;
	private String driver;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Position> positions;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> remarks;

	public Correction() {
	}

	public Correction(final int round, final String driver,
			final int qualPosition, final boolean qualClassified,
			final int racePosition, final boolean raceClassified) {
		this.round = round;
		this.driver = driver;
		positions = new ArrayList<Position>();
		positions.add(new Position(qualPosition, qualClassified));
		positions.add(new Position(racePosition, raceClassified));
	}
}
