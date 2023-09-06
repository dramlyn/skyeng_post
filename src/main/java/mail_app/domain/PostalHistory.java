package mail_app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "postal_history")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostalHistory {
    @EmbeddedId
    private MailingOfficeId id;
    private LocalDateTime arrivalTime;
}
