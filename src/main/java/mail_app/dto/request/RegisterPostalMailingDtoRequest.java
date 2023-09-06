package mail_app.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail_app.domain.enums.MailingStatus;
import mail_app.domain.enums.MailingType;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterPostalMailingDtoRequest {
    @NotNull
    private MailingType type;
    @NotNull
    private int recipientIndex;
    @NotBlank
    private String recipientAddress;
    @NotBlank
    private String recipientName;
    @NotNull
    private int senderIndex;
    @NotBlank
    private String senderAddress;
}
