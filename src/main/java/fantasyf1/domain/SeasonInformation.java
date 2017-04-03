package fantasyf1.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class SeasonInformation {
	@Id
	private String year;
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<Integer, RaceInformation> races;
	private Boolean complete;
	
	public SeasonInformation(final String year) {
		this.year = year;
		this.complete = false;
		races = new HashMap<>();
	}
}
