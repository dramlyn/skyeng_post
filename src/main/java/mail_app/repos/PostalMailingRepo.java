package mail_app.repos;

import mail_app.domain.PostalMailing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalMailingRepo extends CrudRepository<PostalMailing, Long> {
}
