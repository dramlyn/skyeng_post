package mail_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail_app.domain.enums.MailingStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostalMailingHistoryDtoResponse {
    private long id;
    private MailingStatus status;
    private List<PostOfficeInfoDtoResponse> mailingHistory;
}
