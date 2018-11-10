package tvtrader.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class AccountId implements Serializable {

	@NonNull
	private String exchange;

	@NonNull
	private String name;
}
