package tvtrader.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

/**
 * Holds a pair of authentication credentials for an exchange.<br>
 * 
 * @author Wouter
 *
 */
@Data
@Entity
public class ApiCredentials {
	@Id
	@NonNull @NotEmpty
	private String key;

	@NonNull @NotEmpty
	private String secret;
}
