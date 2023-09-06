package mail_app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;
import mail_app.service.PostOfficeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/office")
@Tag(name = "Office API")
public class PostOfficeController {
    private final PostOfficeService officeService;

    public PostOfficeController(PostOfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostOfficeDtoResponse registerPostalOffice(@RequestBody @Valid RegisterPostOfficeDtoRequest request){
        return officeService.registerPostOffice(request);
    }

    @DeleteMapping(value = "/{officeId}")
    public void deletePostalOffice(@PathVariable(name = "officeId") long officeId){
        officeService.deletePostOffice(officeId);
    }
}
