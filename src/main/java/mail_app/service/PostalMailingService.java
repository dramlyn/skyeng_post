package mail_app.service;

import lombok.AllArgsConstructor;
import mail_app.domain.PostOffice;
import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import mail_app.domain.enums.MailingStatus;
import mail_app.dto.request.ArrivalToPostOfficeDtoRequest;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostalMailingDtoResponse;
import mail_app.dto.response.PostalMailingHistoryDtoResponse;
import mail_app.exception.ServerErrorCode;
import mail_app.exception.ServerException;
import mail_app.mapstruct.PostalMailingMapper;
import mail_app.repos.PostalHistoryRepo;
import mail_app.repos.PostalMailingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostalMailingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostalMailing.class);
    private PostalMailingRepo mailingRepo;
    private PostalHistoryRepo postalHistoryRepo;
    private PostService postService;

    public PostalMailingDtoResponse registerMailing(RegisterPostalMailingDtoRequest request) {
        LOGGER.info("Post Mailing Service: register new post mailing with request {}", request);
        postService.findPostOfficeByIndexAndAddress(request.getRecipientIndex(), request.getRecipientAddress());

        int senderOfficeIndex = request.getSenderIndex();
        String senderOfficeAddress = request.getSenderAddress();

        PostOffice senderPostOffice = postService.findPostOfficeByIndexAndAddress(senderOfficeIndex, senderOfficeAddress);

        PostalMailing postalMailing = PostalMailingMapper.INSTANCE.toModel(request);
        postalMailing.setStatus(MailingStatus.ON_WAY);
        PostalMailing savedPostalMailing = mailingRepo.save(postalMailing);

        postService.savePostalHistory(savedPostalMailing, senderPostOffice);

        return PostalMailingMapper.INSTANCE.toDto(savedPostalMailing);
    }

    public PostalMailingDtoResponse arrivalToPostOffice(long mailingId, ArrivalToPostOfficeDtoRequest request) {
        int officeIndex = request.getOfficeIndex();
        LOGGER.info("Post Mailing Service: mailing with id {} arrived to post office with index {}", mailingId, officeIndex);

        PostalMailing mailing = postService.findPostMailingById(mailingId);

        if (mailing.getStatus().equals(MailingStatus.ARRIVED_TO_OFFICE) || mailing.getStatus().equals(MailingStatus.ARRIVED_TO_RECIPIENT)) {
            throw new ServerException("Mailing already arrived to office or recipient", ServerErrorCode.ALREADY_ARRIVED);
        }

        String officeAddress = request.getOfficeAddress();

        PostOffice office = postService.findPostOfficeByIndexAndAddress(officeIndex, officeAddress);

        if (office.getAddress().equals(mailing.getRecipientAddress()) && office.getIndex() == mailing.getRecipientIndex()) {
            mailing.setStatus(MailingStatus.ARRIVED_TO_RECIPIENT);
        } else {
            mailing.setStatus(MailingStatus.ARRIVED_TO_OFFICE);
        }

        postService.savePostalHistory(mailing, office);

        return PostalMailingMapper.INSTANCE.toDto(mailingRepo.save(mailing));
    }

    public PostalMailingDtoResponse leavePostOffice(long mailingId) {
        LOGGER.info("Post Mailing Service: mailing with id {} leaved from post office", mailingId);

        PostalMailing mailing = postService.findPostMailingById(mailingId);
        MailingStatus mailingStatus = mailing.getStatus();

        if (mailingStatus.equals(MailingStatus.ON_WAY)) {
            throw new ServerException("Mailing already on way", ServerErrorCode.ALREADY_ON_WAY);
        }
        if (mailingStatus.equals(MailingStatus.ARRIVED_TO_RECIPIENT)) {
            throw new ServerException("Mailing already arrived to recipient", ServerErrorCode.ALREADY_ARRIVED);
        }

        mailing.setStatus(MailingStatus.ON_WAY);
        return PostalMailingMapper.INSTANCE.toDto(mailingRepo.save(mailing));
    }

    public PostalMailingDtoResponse arrivalToRecipient(long mailingId, ArrivalToPostOfficeDtoRequest request) {
        LOGGER.info("Post Mailing Service: mailing with id {} arrived to recipient post office with index", mailingId);

        PostalMailing mailing = postService.findPostMailingById(mailingId);

        int officeIndex = request.getOfficeIndex();
        String officeAddress = request.getOfficeAddress();

        PostOffice office = postService.findPostOfficeByIndexAndAddress(officeIndex, officeAddress);
        if (office.getIndex() != mailing.getRecipientIndex() || !office.getAddress().equals(mailing.getRecipientAddress())) {
            throw new ServerException(String.format("This is not recipient post office with index %d and address %s",
                    officeIndex, officeAddress), ServerErrorCode.NOT_RECIPIENT_POST_OFFICE);
        }

        if (mailing.getStatus().equals(MailingStatus.ARRIVED_TO_RECIPIENT)) {
            throw new ServerException(String.format("Mailing with id %d is already arrived to recipient", mailingId), ServerErrorCode.ALREADY_ARRIVED);
        }

        mailing.setStatus(MailingStatus.ARRIVED_TO_RECIPIENT);

        postService.savePostalHistory(mailing, office);

        return PostalMailingMapper.INSTANCE.toDto(mailingRepo.save(mailing));
    }

    public PostalMailingHistoryDtoResponse viewMailingHistory(long mailingId) {
        LOGGER.info("Post Mailing Service: view mailing history with id {}", mailingId);

        PostalMailing mailing = postService.findPostMailingById(mailingId);

        List<PostalHistory> postalHistory = postalHistoryRepo.findAllById_PostalMailingOrderByArrivalTimeAsc(mailing);

        return new PostalMailingHistoryDtoResponse(mailingId, mailing.getStatus(),
                postalHistory.stream().map(PostalMailingMapper.INSTANCE::toDto).collect(Collectors.toList()));
    }
}
