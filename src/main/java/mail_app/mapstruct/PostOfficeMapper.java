package mail_app.mapstruct;

import mail_app.domain.PostOffice;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostOfficeMapper {
    PostOfficeMapper INSTANCE = Mappers.getMapper(PostOfficeMapper.class);

    PostOffice toModel(RegisterPostOfficeDtoRequest request);

    PostOfficeDtoResponse toDto(PostOffice office);
}
