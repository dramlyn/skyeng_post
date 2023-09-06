package mail_app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mail_app.domain.enums.MailingStatus;
import mail_app.domain.enums.MailingType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostalMailing {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_mailing"
    )
    private long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MailingType type;
    @Column(nullable = false)
    private int recipientIndex;
    @Column(nullable = false)
    private String recipientAddress;
    @Column(nullable = false)
    private String recipientName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MailingStatus status;
}
