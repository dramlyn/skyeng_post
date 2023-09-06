package mail_app.repos;

import mail_app.domain.PostOffice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostOfficeRepo extends CrudRepository<PostOffice, Long> {
    Optional<PostOffice> findByIndexAndAddress(int index, String address);
}
