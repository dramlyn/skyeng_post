package mail_app.service;

import lombok.AllArgsConstructor;
import mail_app.domain.PostOffice;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;
import mail_app.exception.ServerErrorCode;
import mail_app.exception.ServerException;
import mail_app.repos.PostOfficeRepo;
import mail_app.repos.PostalHistoryRepo;
import mail_app.repos.PostalMailingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static mail_app.TestData.*;
import static org.mockito.Mockito.when;


public class PostOfficeServiceTest {
    private PostalMailingRepo mailingRepo;
    private PostOfficeRepo officeRepo;
    private PostalHistoryRepo historyRepo;
    private PostOfficeService postOfficeService;
    private PostService postService;


    @BeforeEach
    public void init(){
        mailingRepo = mock(PostalMailingRepo.class);
        officeRepo = mock(PostOfficeRepo.class);
        historyRepo = mock(PostalHistoryRepo.class);
        postService = new PostService(officeRepo, mailingRepo, historyRepo);
        postOfficeService = new PostOfficeService(officeRepo, postService);
    }

    @Test
    public void registerPostOfficeTest(){
        RegisterPostOfficeDtoRequest registerPostOfficeDtoRequest = registerPostOfficeDtoRequest();

        PostOffice postOfficeBeforeInsert = postOffice(0);
        PostOffice postOfficeAfterInsert = postOffice(1);

        when(officeRepo.save(postOfficeBeforeInsert)).thenReturn(postOfficeAfterInsert);

        PostOfficeDtoResponse expected = new PostOfficeDtoResponse(1, 1, "name", "address");
        PostOfficeDtoResponse actual = postOfficeService.registerPostOffice(registerPostOfficeDtoRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void deletePostOfficeTest_postOfficeNotFound(){
        when(officeRepo.findById(1L)).thenReturn(Optional.empty());

        ServerException exc = assertThrows(ServerException.class, () -> postOfficeService.deletePostOffice(1));

        assertEquals(ServerErrorCode.POST_OFFICE_NOT_FOUND, exc.getErrorCode());
    }
}