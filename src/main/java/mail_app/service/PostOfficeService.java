package mail_app.service;

import lombok.AllArgsConstructor;
import mail_app.domain.PostOffice;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;
import mail_app.mapstruct.PostOfficeMapper;
import mail_app.repos.PostOfficeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostOfficeService {
    private final PostOfficeRepo officeRepo;
    private final PostService postService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostOfficeService.class);

    public PostOfficeDtoResponse registerPostOffice(RegisterPostOfficeDtoRequest request){
        LOGGER.info("Post Office Service: register new post office with request {}", request);

        PostOffice office = PostOfficeMapper.INSTANCE.toModel(request);
        return PostOfficeMapper.INSTANCE.toDto(officeRepo.save(office));
    }

    public void deletePostOffice(long officeId) {
        LOGGER.info("Post Office Service: delete post office with id {}", officeId);

        officeRepo.delete(postService.findPostOfficeById(officeId));
    }
}
