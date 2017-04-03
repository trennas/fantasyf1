package fantasyf1.domain;

import java.util.Date;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RaceInformation {
	private String grandPrixName;
	private String circuitName;
	private String country;
	private String location;
	private Date dateTime;
}
