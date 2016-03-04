package fantasyf1.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Config {
	@Id
	private String key;
	private String value;

	public Config() {
	}

	public Config(final String key, final String value) {
		this.key = key;
		this.value = value;
	}
}
