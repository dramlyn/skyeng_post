package mail_app;

import mail_app.domain.MailingOfficeId;
import mail_app.domain.PostOffice;
import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import mail_app.domain.enums.MailingStatus;
import mail_app.domain.enums.MailingType;
import mail_app.dto.request.ArrivalToPostOfficeDtoRequest;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;
import mail_app.dto.response.PostOfficeInfoDtoResponse;
import mail_app.dto.response.PostalMailingDtoResponse;
import mail_app.dto.response.PostalMailingHistoryDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

public class TestData {
    // MAILING DTO'S
    public static RegisterPostalMailingDtoRequest registerPostalMailingDtoRequest(MailingType type, int recipientIndex, String recipientAddress, String recipientName, int senderIndex, String senderAddress){
        return new RegisterPostalMailingDtoRequest(type, recipientIndex, recipientAddress, recipientName, senderIndex, senderAddress);
    }

    public static RegisterPostalMailingDtoRequest registerPostalMailingDtoRequest(){
        return registerPostalMailingDtoRequest(MailingType.PACKAGE, 3, "rec_address", "name", 1, "sender_address");
    }

    public static PostalMailingDtoResponse postalMailingDtoResponse(long id, MailingType type, int recipientIndex, String recipientAddress, String recipientName, MailingStatus status){
        return new PostalMailingDtoResponse(id, type, recipientIndex, recipientAddress, recipientName, status);
    }

    public static PostalMailingDtoResponse postalMailingDtoResponse(){
        return postalMailingDtoResponse(1, MailingType.PACKAGE, 3, "rec_address", "name", MailingStatus.ON_WAY);
    }

    public static PostalMailingDtoResponse postalMailingDtoResponse(MailingStatus status){
        return postalMailingDtoResponse(1, MailingType.PACKAGE, 3, "rec_address", "name", status);
    }

    public static ArrivalToPostOfficeDtoRequest arrivalToPostOfficeDtoRequest(int officeIndex, String officeAddress){
        return new ArrivalToPostOfficeDtoRequest(officeIndex, officeAddress);
    }

    public static ArrivalToPostOfficeDtoRequest arrivalToPostOfficeDtoRequest(){
        return new ArrivalToPostOfficeDtoRequest(1, "address");
    }

    public static PostalMailingHistoryDtoResponse postalMailingHistoryDtoResponse(long id, MailingStatus status, List<PostOfficeInfoDtoResponse> mailingHistory){
        return new PostalMailingHistoryDtoResponse(id, status, mailingHistory);
    }

    public static PostalMailingHistoryDtoResponse postalMailingHistoryDtoResponse() throws InterruptedException {
        LocalDateTime firstTime = LocalDateTime.now();
        Thread.sleep(5);
        LocalDateTime secondTime = LocalDateTime.now();
        Thread.sleep(5);
        LocalDateTime thirdTime = LocalDateTime.now();
        Thread.sleep(5);
        return postalMailingHistoryDtoResponse(1, MailingStatus.ON_WAY, List.of(new
                        PostOfficeInfoDtoResponse(1, "address", firstTime),
                new PostOfficeInfoDtoResponse(2, "address", secondTime),
                new PostOfficeInfoDtoResponse(3, "address", thirdTime)));
    }

    //OFFICE DTO'S

    public static RegisterPostOfficeDtoRequest registerPostOfficeDtoRequest(int index, String name, String address){
        return new RegisterPostOfficeDtoRequest(index, name, address);
    }

    public static RegisterPostOfficeDtoRequest registerPostOfficeDtoRequest(){
        return new RegisterPostOfficeDtoRequest(1, "name", "address");
    }

    public static RegisterPostOfficeDtoRequest registerPostOfficeDtoRequest(int index){
        return new RegisterPostOfficeDtoRequest(index, "name", "address");
    }

    public static PostOfficeDtoResponse postOfficeDtoResponse(long id, int index, String name, String address){
        return new PostOfficeDtoResponse(id, index, name, address);
    }

    public static PostOfficeDtoResponse postOfficeDtoResponse(){
        return new PostOfficeDtoResponse(1, 1, "name", "address");
    }

    //POST OFFICE MODEL

    public static PostOffice postOffice(long id, int index, String address, String name){
        return new PostOffice(id, index, address, name);
    }

    public static PostOffice postOffice(){
        return new PostOffice(1, 1, "address", "name");
    }

    public static PostOffice postOffice(int id){
        return new PostOffice(id, 1, "address", "name");
    }

    //POSTAL MAILING MODEL

    public static PostalMailing postalMailing(long id, MailingType type, int recipientIndex, String recipientAddress, String recipientName, MailingStatus status){
        return new PostalMailing(id, type, recipientIndex, recipientAddress, recipientName, status);
    }

    public static PostalMailing postalMailing(){
        return new PostalMailing(1, MailingType.PACKAGE, 3, "rec_address", "name", MailingStatus.ON_WAY);
    }

    public static PostalMailing postalMailing(long id){
        return new PostalMailing(id, MailingType.PACKAGE, 3, "rec_address", "name", MailingStatus.ON_WAY);
    }

    //POSTAL HISTORY MODEL

    public static PostalHistory postalHistory(MailingOfficeId id, LocalDateTime arrivalTime){
        return new PostalHistory(id, arrivalTime);
    }

    public static PostalHistory postalHistory(PostOffice postOffice, PostalMailing postalMailing){
        return new PostalHistory(new MailingOfficeId(postalMailing, postOffice), LocalDateTime.now());
    }
}
