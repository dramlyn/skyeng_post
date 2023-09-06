package mail_app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mail_app.domain.PostalMailing;
import mail_app.dto.request.ArrivalToPostOfficeDtoRequest;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostalMailingDtoResponse;
import mail_app.dto.response.PostalMailingHistoryDtoResponse;
import mail_app.service.PostalMailingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mailing")
@Tag(name = "Mailing API")
@AllArgsConstructor
public class PostMailingController {
    private final PostalMailingService mailingService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostalMailingDtoResponse registerMailing(@RequestBody @Valid RegisterPostalMailingDtoRequest request){
        return mailingService.registerMailing(request);
    }

    @PutMapping(value = "/{mailingId}/arrivalToPostOffice", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostalMailingDtoResponse arrivalToIntermediatePostOffice(@PathVariable("mailingId") long mailingId, @RequestBody @Valid ArrivalToPostOfficeDtoRequest request){
        return mailingService.arrivalToPostOffice(mailingId, request);
    }

    @PutMapping(value = "/{mailingId}/leavePostOffice", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostalMailingDtoResponse leavePostOffice(@PathVariable("mailingId") long mailingId){
        return mailingService.leavePostOffice(mailingId);
    }

    @PutMapping(value = "/{mailingId}/arrivalToRecipient", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostalMailingDtoResponse arrivalToRecipient(@PathVariable("mailingId") long mailingId, @RequestBody @Valid ArrivalToPostOfficeDtoRequest request){
        return mailingService.arrivalToRecipient(mailingId, request);
    }

    @GetMapping(value = "/{mailingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostalMailingHistoryDtoResponse findPostalMailingHistory(@PathVariable("mailingId") long mailingId){
        return mailingService.viewMailingHistory(mailingId);
    }
}
