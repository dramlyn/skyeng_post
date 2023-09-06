package mail_app.mapstruct;

import javax.annotation.processing.Generated;
import mail_app.domain.MailingOfficeId;
import mail_app.domain.PostOffice;
import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostOfficeInfoDtoResponse;
import mail_app.dto.response.PostalMailingDtoResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-06T17:50:08+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
public class PostalMailingMapperImpl implements PostalMailingMapper {

    @Override
    public PostalMailing toModel(RegisterPostalMailingDtoRequest request) {
        if ( request == null ) {
            return null;
        }

        PostalMailing postalMailing = new PostalMailing();

        postalMailing.setType( request.getType() );
        postalMailing.setRecipientIndex( request.getRecipientIndex() );
        postalMailing.setRecipientAddress( request.getRecipientAddress() );
        postalMailing.setRecipientName( request.getRecipientName() );

        return postalMailing;
    }

    @Override
    public PostalMailingDtoResponse toDto(PostalMailing mailing) {
        if ( mailing == null ) {
            return null;
        }

        PostalMailingDtoResponse postalMailingDtoResponse = new PostalMailingDtoResponse();

        postalMailingDtoResponse.setId( mailing.getId() );
        postalMailingDtoResponse.setType( mailing.getType() );
        postalMailingDtoResponse.setRecipientIndex( mailing.getRecipientIndex() );
        postalMailingDtoResponse.setRecipientAddress( mailing.getRecipientAddress() );
        postalMailingDtoResponse.setRecipientName( mailing.getRecipientName() );
        postalMailingDtoResponse.setStatus( mailing.getStatus() );

        return postalMailingDtoResponse;
    }

    @Override
    public PostOfficeInfoDtoResponse toDto(PostalHistory postalHistory) {
        if ( postalHistory == null ) {
            return null;
        }

        PostOfficeInfoDtoResponse postOfficeInfoDtoResponse = new PostOfficeInfoDtoResponse();

        postOfficeInfoDtoResponse.setOfficeIndex( postalHistoryIdPostOfficeIndex( postalHistory ) );
        postOfficeInfoDtoResponse.setAddress( postalHistoryIdPostOfficeAddress( postalHistory ) );
        postOfficeInfoDtoResponse.setArrivalTime( postalHistory.getArrivalTime() );

        return postOfficeInfoDtoResponse;
    }

    private int postalHistoryIdPostOfficeIndex(PostalHistory postalHistory) {
        if ( postalHistory == null ) {
            return 0;
        }
        MailingOfficeId id = postalHistory.getId();
        if ( id == null ) {
            return 0;
        }
        PostOffice postOffice = id.getPostOffice();
        if ( postOffice == null ) {
            return 0;
        }
        int index = postOffice.getIndex();
        return index;
    }

    private String postalHistoryIdPostOfficeAddress(PostalHistory postalHistory) {
        if ( postalHistory == null ) {
            return null;
        }
        MailingOfficeId id = postalHistory.getId();
        if ( id == null ) {
            return null;
        }
        PostOffice postOffice = id.getPostOffice();
        if ( postOffice == null ) {
            return null;
        }
        String address = postOffice.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }
}
