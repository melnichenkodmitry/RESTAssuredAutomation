package reqres.in.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqRegSuccessful{
	private String email;
	private String password;

	public ReqRegSuccessful(String email) {
		this.email = email;
	}
}
