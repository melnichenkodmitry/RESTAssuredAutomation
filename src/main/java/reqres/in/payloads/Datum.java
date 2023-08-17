package reqres.in.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Datum {
    public int id;
    public String email;
    public String first_name;
    public String last_name;
    public String avatar;
}
