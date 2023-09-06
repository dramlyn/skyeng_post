package mail_app.mapstruct;

import mail_app.domain.PostalHistory;
import mail_app.domain.PostalMailing;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostOfficeInfoDtoResponse;
import mail_app.dto.response.PostalMailingDtoResponse;
import mail_app.dto.response.PostalMailingHistoryDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostalMailingMapper {
    PostalMailingMapper INSTANCE = Mappers.getMapper(PostalMailingMapper.class);

    PostalMailing toModel(RegisterPostalMailingDtoRequest request);

    PostalMailingDtoResponse toDto(PostalMailing mailing);

    @Mapping(source = "postalHistory.id.postOffice.index", target = "officeIndex")
    @Mapping(source = "postalHistory.id.postOffice.address", target = "address")
    @Mapping(source = "postalHistory.arrivalTime", target = "arrivalTime")
    PostOfficeInfoDtoResponse toDto(PostalHistory postalHistory);
}
