package mail_app.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailingOfficeId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "mailing_id", nullable = false)
    private PostalMailing postalMailing;
    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private PostOffice postOffice;
}
