package mail_app.repos;

import mail_app.domain.MailingOfficeId;
import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostalHistoryRepo extends CrudRepository<PostalHistory, MailingOfficeId> {
    List<PostalHistory> findAllById_PostalMailingOrderByArrivalTimeAsc(PostalMailing mailingId);
}
