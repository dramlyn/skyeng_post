package mail_app.mapstruct;

import javax.annotation.processing.Generated;
import mail_app.domain.PostOffice;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-06T17:50:08+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
public class PostOfficeMapperImpl implements PostOfficeMapper {

    @Override
    public PostOffice toModel(RegisterPostOfficeDtoRequest request) {
        if ( request == null ) {
            return null;
        }

        PostOffice postOffice = new PostOffice();

        postOffice.setIndex( request.getIndex() );
        postOffice.setAddress( request.getAddress() );
        postOffice.setName( request.getName() );

        return postOffice;
    }

    @Override
    public PostOfficeDtoResponse toDto(PostOffice office) {
        if ( office == null ) {
            return null;
        }

        PostOfficeDtoResponse postOfficeDtoResponse = new PostOfficeDtoResponse();

        postOfficeDtoResponse.setId( office.getId() );
        postOfficeDtoResponse.setIndex( office.getIndex() );
        postOfficeDtoResponse.setName( office.getName() );
        postOfficeDtoResponse.setAddress( office.getAddress() );

        return postOfficeDtoResponse;
    }
}
