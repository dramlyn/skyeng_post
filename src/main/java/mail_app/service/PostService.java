package mail_app.service;

import lombok.AllArgsConstructor;
import mail_app.domain.MailingOfficeId;
import mail_app.domain.PostOffice;
import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import mail_app.exception.ServerErrorCode;
import mail_app.exception.ServerException;
import mail_app.repos.PostOfficeRepo;
import mail_app.repos.PostalHistoryRepo;
import mail_app.repos.PostalMailingRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PostService {
    private final PostOfficeRepo officeRepo;
    private final PostalMailingRepo mailingRepo;
    private final PostalHistoryRepo postalHistoryRepo;

    public PostOffice findPostOfficeById(long id) {
        return officeRepo.findById(id).orElseThrow(
                () -> new ServerException(String.format("Post office with id %d not found", id), ServerErrorCode.POST_OFFICE_NOT_FOUND));
    }

    public PostalMailing findPostMailingById(long id) {
        return mailingRepo.findById(id).orElseThrow(
                () -> new ServerException(String.format("Post mailing with id %d not found", id), ServerErrorCode.POST_MAILING_NOT_FOUND));
    }

    public PostOffice findPostOfficeByIndexAndAddress(int index, String address){
        return officeRepo.findByIndexAndAddress(index, address)
                .orElseThrow(() -> new ServerException(String.format("Post office with index %d and address %s not found",
                        index, address), ServerErrorCode.POST_OFFICE_NOT_FOUND));
    }

    public PostalHistory savePostalHistory(PostalMailing mailing, PostOffice office){
        MailingOfficeId mailingOfficeId = new MailingOfficeId(mailing, office);
        PostalHistory postalHistory = new PostalHistory(mailingOfficeId, LocalDateTime.now());

        return postalHistoryRepo.save(postalHistory);
    }
}
