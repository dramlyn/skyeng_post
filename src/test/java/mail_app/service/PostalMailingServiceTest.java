package mail_app.service;

import mail_app.domain.PostOffice;
import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import mail_app.domain.enums.MailingStatus;
import mail_app.domain.enums.MailingType;
import mail_app.dto.request.ArrivalToPostOfficeDtoRequest;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostalMailingDtoResponse;
import mail_app.dto.response.PostalMailingHistoryDtoResponse;
import mail_app.exception.ServerErrorCode;
import mail_app.exception.ServerException;
import mail_app.repos.PostOfficeRepo;
import mail_app.repos.PostalHistoryRepo;
import mail_app.repos.PostalMailingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static mail_app.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostalMailingServiceTest {
    private PostalMailingRepo mailingRepo;
    private PostOfficeRepo officeRepo;
    private PostalHistoryRepo historyRepo;
    private PostalMailingService postalMailingService;
    private PostService postService;

    @BeforeEach
    public void init() {
        mailingRepo = mock(PostalMailingRepo.class);
        officeRepo = mock(PostOfficeRepo.class);
        historyRepo = mock(PostalHistoryRepo.class);
        postService = new PostService(officeRepo, mailingRepo, historyRepo);
        postalMailingService = new PostalMailingService(mailingRepo, historyRepo, postService);
    }

    @Test
    public void registerPostalMailingTest() {
        RegisterPostalMailingDtoRequest request = registerPostalMailingDtoRequest();

        PostalMailing postalMailingBeforeSave = postalMailing(0);
        PostalMailing postalMailingAfterSave = postalMailing(1);

        PostOffice senderPostOffice = postOffice(1, 1, "sender_address", "sender_name");
        PostOffice recipientPostOffice = postOffice(3, 3, "rec_address", "name");

        when(officeRepo.findByIndexAndAddress(request.getSenderIndex(), request.getSenderAddress())).thenReturn(Optional.of(senderPostOffice));
        when(officeRepo.findByIndexAndAddress(request.getRecipientIndex(), request.getRecipientAddress())).thenReturn(Optional.of(recipientPostOffice));
        when(mailingRepo.save(postalMailingBeforeSave)).thenReturn(postalMailingAfterSave);

        PostalMailingDtoResponse expected = new PostalMailingDtoResponse(1, MailingType.PACKAGE, 3, "rec_address", "name", MailingStatus.ON_WAY);
        PostalMailingDtoResponse actual = postalMailingService.registerMailing(request);

        assertEquals(expected, actual);
    }

    @Test
    public void registerPostalMailingTest_senderPostOfficeNotFound() {
        RegisterPostalMailingDtoRequest request = registerPostalMailingDtoRequest();
        when(officeRepo.findByIndexAndAddress(request.getSenderIndex(), request.getSenderAddress())).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.registerMailing(request));

        assertEquals(ServerErrorCode.POST_OFFICE_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void registerPostalMailingTest_recipientPostOfficeNotFound() {
        RegisterPostalMailingDtoRequest request = registerPostalMailingDtoRequest();

        PostOffice senderPostOffice = postOffice(1);

        when(officeRepo.findByIndexAndAddress(request.getSenderIndex(), request.getSenderAddress())).thenReturn(Optional.of(senderPostOffice));
        when(officeRepo.findByIndexAndAddress(request.getRecipientIndex(), request.getRecipientAddress())).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.registerMailing(request));

        assertEquals(ServerErrorCode.POST_OFFICE_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void arrivalToPostOfficeTest() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostOffice postOffice = postOffice(2, 2, "sec_address", "name");

        PostalMailing postalMailing = postalMailing();

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));
        when(officeRepo.findByIndexAndAddress(2, "sec_address")).thenReturn(Optional.of(postOffice));

        when(mailingRepo.save(postalMailing)).thenReturn(postalMailing);

        PostalMailingDtoResponse expected = postalMailingDtoResponse(1, MailingType.PACKAGE, 3, "rec_address", "name", MailingStatus.ARRIVED_TO_OFFICE);
        PostalMailingDtoResponse actual = postalMailingService.arrivalToPostOffice(1, request);

        assertEquals(expected, actual);
    }

    @Test
    public void arrivalToPostOfficeTest_mailingNotFound() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        when(mailingRepo.findById(1L)).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToPostOffice(1, request));

        assertEquals(ServerErrorCode.POST_MAILING_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void arrivalToPostOfficeTest_alreadyArrivedToPostOfficeOrRecipient() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostalMailing firstPostalMailing = postalMailing(1);
        firstPostalMailing.setStatus(MailingStatus.ARRIVED_TO_OFFICE);

        PostalMailing secondPostalMailing = postalMailing(2);
        secondPostalMailing.setStatus(MailingStatus.ARRIVED_TO_RECIPIENT);

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(firstPostalMailing));
        when(mailingRepo.findById(2L)).thenReturn(Optional.of(secondPostalMailing));

        ServerException alreadyArrivedToPostOfficeExc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToPostOffice(1, request));
        ServerException alreadyArrivedToRecipientExc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToPostOffice(2, request));


        assertEquals(ServerErrorCode.ALREADY_ARRIVED, alreadyArrivedToPostOfficeExc.getErrorCode());
        assertEquals(ServerErrorCode.ALREADY_ARRIVED, alreadyArrivedToRecipientExc.getErrorCode());
    }

    @Test
    public void arrivalToPostOfficeTest_notFoundIntermediatePostOffice() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostalMailing postalMailing = postalMailing();

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));
        when(officeRepo.findByIndexAndAddress(2, "sec_address")).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToPostOffice(1, request));

        assertEquals(ServerErrorCode.POST_OFFICE_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void leavePostOfficeTest() {
        PostalMailing mailing = postalMailing();
        mailing.setStatus(MailingStatus.ARRIVED_TO_OFFICE);

        when(mailingRepo.findById(mailing.getId())).thenReturn(Optional.of(mailing));

        when(mailingRepo.save(mailing)).thenReturn(mailing);

        PostalMailingDtoResponse expected = postalMailingDtoResponse(MailingStatus.ON_WAY);
        PostalMailingDtoResponse actual = postalMailingService.leavePostOffice(1);

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void leavePostOfficeTest_postMailingNotFound() {
        when(mailingRepo.findById(1L)).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.leavePostOffice(1));

        assertEquals(ServerErrorCode.POST_MAILING_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void leavePostOfficeTest_postMailingAlreadyOnWay() {
        PostalMailing mailing = postalMailing();

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(mailing));

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.leavePostOffice(1));

        assertEquals(ServerErrorCode.ALREADY_ON_WAY, exc.getErrorCode());
    }

    @Test
    public void leavePostOfficeTest_postMailingAlreadyArrivedToRecipient() {
        PostalMailing mailing = postalMailing();
        mailing.setStatus(MailingStatus.ARRIVED_TO_RECIPIENT);

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(mailing));

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.leavePostOffice(1));

        assertEquals(ServerErrorCode.ALREADY_ARRIVED, exc.getErrorCode());
    }

    @Test
    public void arrivalToRecipientTest() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostOffice postOffice = postOffice(3, 3, "rec_address", "name");

        PostalMailing postalMailing = postalMailing();

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));
        when(officeRepo.findByIndexAndAddress(2, "sec_address")).thenReturn(Optional.of(postOffice));

        when(mailingRepo.save(postalMailing)).thenReturn(postalMailing);

        PostalMailingDtoResponse expected = postalMailingDtoResponse(1, MailingType.PACKAGE, 3, "rec_address", "name", MailingStatus.ARRIVED_TO_RECIPIENT);
        PostalMailingDtoResponse actual = postalMailingService.arrivalToRecipient(1, request);

        assertEquals(expected, actual);
    }

    @Test
    public void arrivalToRecipientTest_mailingNotFound() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        when(mailingRepo.findById(1L)).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToRecipient(1, request));

        assertEquals(ServerErrorCode.POST_MAILING_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void arrivalToRecipientTest_alreadyArrivedToRecipient() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostalMailing postalMailing = postalMailing(1);
        postalMailing.setStatus(MailingStatus.ARRIVED_TO_RECIPIENT);

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToPostOffice(1, request));

        assertEquals(ServerErrorCode.ALREADY_ARRIVED, exc.getErrorCode());
    }

    @Test
    public void arrivalToRecipientTest_notFoundRecipientPostOffice() {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostalMailing postalMailing = postalMailing();

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));
        when(officeRepo.findByIndexAndAddress(2, "sec_address")).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToRecipient(1, request));

        assertEquals(ServerErrorCode.POST_OFFICE_NOT_FOUND, exc.getErrorCode());
    }

    @Test
    public void arrivalToRecipientTest_notRecipientPostOffice() {
        PostalMailing postalMailing = postalMailing();

        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest(2, "sec_address");

        PostOffice postOffice = postOffice(2, 2, "address", "name");

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));
        when(officeRepo.findByIndexAndAddress(2, "sec_address")).thenReturn(Optional.of(postOffice));

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.arrivalToRecipient(1, request));

        assertEquals(ServerErrorCode.NOT_RECIPIENT_POST_OFFICE, exc.getErrorCode());
    }

    @Test
    public void viewMailingHistoryTest() {
        PostalMailing postalMailing = postalMailing();
        PostOffice firstPostOffice = postOffice(1, 1, "frst_address", "first_name");
        PostOffice secondPostOffice = postOffice(2, 2, "second_address", "second_name");

        PostalHistory firstPostalHistory = postalHistory(firstPostOffice, postalMailing);
        PostalHistory secondPostalHistory = postalHistory(secondPostOffice, postalMailing);

        when(mailingRepo.findById(1L)).thenReturn(Optional.of(postalMailing));
        when(historyRepo.findAllById_PostalMailingOrderByArrivalTimeAsc(postalMailing)).thenReturn(List.of(firstPostalHistory, secondPostalHistory));

        PostalMailingHistoryDtoResponse response = postalMailingService.viewMailingHistory(1);

        assertAll(
                () -> assertEquals(1, response.getId()),
                () -> assertEquals(MailingStatus.ON_WAY, response.getStatus()),
                () -> assertEquals(2, response.getMailingHistory().size())
        );
    }

    @Test
    public void viewMailingHistoryTest_mailingNotFound(){
        when(mailingRepo.findById(1L)).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postalMailingService.viewMailingHistory(1));

        assertEquals(ServerErrorCode.POST_MAILING_NOT_FOUND, exc.getErrorCode());
    }
}