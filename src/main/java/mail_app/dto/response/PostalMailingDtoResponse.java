package mail_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail_app.domain.enums.MailingStatus;
import mail_app.domain.enums.MailingType;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class PostalMailingDtoResponse {
    private long id;
    private MailingType type;
    private int recipientIndex;
    private String recipientAddress;
    private String recipientName;
    private MailingStatus status;
}
