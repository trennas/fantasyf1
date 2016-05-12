package fantasyf1.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class Position implements Comparable<Position> {
	@Getter
	@Setter
	private int position;
	@Getter
	@Setter
	private boolean classified;
	@Getter
	@Setter
	private int driverNumber;
	@Getter
	@Setter
	private String carName;

	public Position() {
	}

	public Position(final int position, final boolean classified, final int driverNumber, final String carName) {
		this.position = position;
		this.classified = classified;
		this.driverNumber = driverNumber;
		this.carName = carName;
	}

	@Override
	public int compareTo(final Position o) {
		if (classified == true && o.classified == false) {
			return -1;
		} else if (classified == false && o.classified == true) {
			return -1;
		} else if (position < o.position) {
			return -1;
		} else if (position == o.position) {
			return 0;
		} else {
			return 1;
		}
	}
}
