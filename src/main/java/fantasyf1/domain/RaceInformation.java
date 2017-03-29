package fantasyf1.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class RaceInformation {
	private String grandPrixName;
	private String circuitName;
	private String country;
	private String location;
	private LocalDate date;
	private LocalTime time;
}
