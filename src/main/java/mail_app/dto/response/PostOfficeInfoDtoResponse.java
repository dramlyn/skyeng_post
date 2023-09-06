package mail_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostOfficeInfoDtoResponse {
    private int officeIndex;
    private String address;
    private LocalDateTime arrivalTime;
}
