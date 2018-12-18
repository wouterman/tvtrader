package tvtrader.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class AccountId implements Serializable {

	@NonNull
	private String exchange;

	@NonNull
	private String name;
}
