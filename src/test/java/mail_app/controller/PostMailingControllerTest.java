package mail_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mail_app.domain.enums.MailingStatus;
import mail_app.domain.enums.MailingType;
import mail_app.dto.request.ArrivalToPostOfficeDtoRequest;
import mail_app.dto.request.RegisterPostalMailingDtoRequest;
import mail_app.dto.response.PostalMailingDtoResponse;
import mail_app.dto.response.PostalMailingHistoryDtoResponse;
import mail_app.handler.GlobalErrorHandler;
import mail_app.service.PostalMailingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static mail_app.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PostMailingController.class)
public class PostMailingControllerTest {
    @MockBean
    private PostalMailingService mailingService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final String baseUrl = "/api/mailing";

    public static Stream<Arguments> makeWrongRegisterPostalMailingDtoRequest() {
        return Stream.of(
                Arguments.arguments(registerPostalMailingDtoRequest(null, 1, "address", "name", 2, "addd")),
                Arguments.arguments(registerPostalMailingDtoRequest(MailingType.LETTER, 1, null, "name", 2, "addd")),
                Arguments.arguments(registerPostalMailingDtoRequest(MailingType.LETTER, 1, "address", null, 2, "addd")),
                Arguments.arguments(registerPostalMailingDtoRequest(MailingType.LETTER, 1, "address", "name", 2, null)),
                Arguments.arguments(registerPostalMailingDtoRequest(MailingType.LETTER, 1, "", "name", 2, "addd")),
                Arguments.arguments(registerPostalMailingDtoRequest(MailingType.LETTER, 1, "address", "", 2, "addd")),
                Arguments.arguments(registerPostalMailingDtoRequest(MailingType.LETTER, 1, "address", "name", 2, ""))
        );
    }

    public static Stream<Arguments> makeWrongRegisterArrivalToPostOfficeDtoRequest(){
        return Stream.of(
                Arguments.arguments(arrivalToPostOfficeDtoRequest(1, null)),
                Arguments.arguments(arrivalToPostOfficeDtoRequest(1, ""))
        );
    }

    @Test
    public void registerMailingTest() throws Exception {
        RegisterPostalMailingDtoRequest request = registerPostalMailingDtoRequest();
        PostalMailingDtoResponse response = postalMailingDtoResponse();

        Mockito.when(mailingService.registerMailing(request)).thenReturn(response);
        MvcResult result = postMailing(request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), PostalMailingDtoResponse.class));
    }

    @ParameterizedTest
    @MethodSource("makeWrongRegisterPostalMailingDtoRequest")
    public void registerMailingTest_wrongData(RegisterPostalMailingDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(postMailing(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    public void arrivalToIntermediatePostOfficeTest() throws Exception {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest();
        PostalMailingDtoResponse response = postalMailingDtoResponse(MailingStatus.ARRIVED_TO_OFFICE);

        Mockito.when(mailingService.arrivalToPostOffice(1, request)).thenReturn(response);

        MvcResult result = arrivalToIntermediatePostOffice(request, 1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), PostalMailingDtoResponse.class));
    }

    @ParameterizedTest
    @MethodSource("makeWrongRegisterArrivalToPostOfficeDtoRequest")
    public void arrivalToIntermediatePostOfficeTest_wrongData(ArrivalToPostOfficeDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(arrivalToIntermediatePostOffice(request, 1, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    public void arrivalToRecipientTest() throws Exception {
        ArrivalToPostOfficeDtoRequest request = arrivalToPostOfficeDtoRequest();
        PostalMailingDtoResponse response = postalMailingDtoResponse(MailingStatus.ARRIVED_TO_RECIPIENT);

        Mockito.when(mailingService.arrivalToRecipient(1, request)).thenReturn(response);

        MvcResult result = arrivalToRecipient(request, 1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), PostalMailingDtoResponse.class));
    }

    @ParameterizedTest
    @MethodSource("makeWrongRegisterArrivalToPostOfficeDtoRequest")
    public void arrivalToRecipientTest_wrongData(ArrivalToPostOfficeDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(arrivalToRecipient(request, 1, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    public void leavePostOfficeTest() throws Exception {
        PostalMailingDtoResponse response = postalMailingDtoResponse(MailingStatus.ON_WAY);

        Mockito.when(mailingService.leavePostOffice(1)).thenReturn(response);

        MvcResult result = leavePostOffice(1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), PostalMailingDtoResponse.class));
    }

    @Test
    public void viewMailingHistoryTest() throws Exception{
        PostalMailingHistoryDtoResponse response = postalMailingHistoryDtoResponse();

        Mockito.when(mailingService.viewMailingHistory(1)).thenReturn(response);
        MvcResult result = viewMailingHistory(1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), PostalMailingHistoryDtoResponse.class));
    }

    private MvcResult postMailing(RegisterPostalMailingDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult arrivalToIntermediatePostOffice(ArrivalToPostOfficeDtoRequest request, long mailingId, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseUrl + "/" + mailingId + "/arrivalToPostOffice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult arrivalToRecipient(ArrivalToPostOfficeDtoRequest request, long mailingId, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseUrl + "/" + mailingId + "/arrivalToRecipient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult leavePostOffice(long mailingId, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseUrl + "/" + mailingId + "/leavePostOffice")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult viewMailingHistory(long mailingId, ResultMatcher status) throws Exception {
        return mvc.perform(get(baseUrl + "/" + mailingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andReturn();
    }
}