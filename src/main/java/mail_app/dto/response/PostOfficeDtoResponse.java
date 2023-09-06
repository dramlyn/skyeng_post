package mail_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostOfficeDtoResponse {
    private long id;
    private int index;
    private String name;
    private String address;
}
