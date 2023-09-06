package mail_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mail_app.dto.request.RegisterPostOfficeDtoRequest;
import mail_app.dto.response.PostOfficeDtoResponse;
import mail_app.handler.GlobalErrorHandler;
import mail_app.service.PostOfficeService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static mail_app.TestData.*;

@WebMvcTest(PostOfficeController.class)
public class PostOfficeControllerTest {
    @MockBean
    private PostOfficeService officeService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final String baseUrl = "/api/office";

    public static Stream<Arguments> makeWrongRegisterPostOfficeDtoRequest() {
        return Stream.of(
                Arguments.arguments(registerPostOfficeDtoRequest( 1, null, "address")),
                Arguments.arguments(registerPostOfficeDtoRequest( 1, "name", null)),
                Arguments.arguments(registerPostOfficeDtoRequest( 1, "", "address")),
                Arguments.arguments(registerPostOfficeDtoRequest( 1, "name", ""))
        );
    }

    @Test
    public void registerPostOfficeTest() throws Exception {
        RegisterPostOfficeDtoRequest request = registerPostOfficeDtoRequest();
        PostOfficeDtoResponse response = postOfficeDtoResponse();

        Mockito.when(officeService.registerPostOffice(request)).thenReturn(response);
        MvcResult result = registerPostOffice(request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), PostOfficeDtoResponse.class));
    }

    @ParameterizedTest
    @MethodSource("makeWrongRegisterPostOfficeDtoRequest")
    public void registerPostOfficeTest_wrongData(RegisterPostOfficeDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(registerPostOffice(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    public void deletePostOfficeTest() throws Exception {
        Mockito.doNothing().when(officeService).deletePostOffice(1);

        deletePostOffice(1, status().isOk());
    }



    private MvcResult registerPostOffice(RegisterPostOfficeDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult deletePostOffice(long officeId, ResultMatcher status) throws Exception {
        return mvc.perform(delete(baseUrl + "/" + officeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status)
                .andReturn();
    }
}