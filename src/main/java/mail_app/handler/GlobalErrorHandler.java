package mail_app.handler;

import mail_app.exception.ServerErrorCode;
import mail_app.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsResponse handleValidation(MethodArgumentNotValidException exc) {
        LOGGER.warn(exc.getMessage());
        final ErrorsResponse errResponse = new ErrorsResponse();
        exc.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errResponse.getErrorResponses().add(new ErrorResponse("WRONG_" + fieldError.
                    getField().toUpperCase(),
                    fieldError.getDefaultMessage()));
        });

        return errResponse;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleValidation(DataAccessException ex) {
        LOGGER.warn(ex.getMessage());

        return new ErrorResponse(ServerErrorCode.DATABASE_EXCEPTION.toString(), ex.getCause().getCause().getMessage());
    }

    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleValidation(ServerException ex) {
        LOGGER.warn(ex.getMessage());

        return new ErrorResponse(ex.getErrorCode().toString(), ex.getMessage());
    }

    /**
     * Handle a database error leading to conflict of existing application data.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrity(DataIntegrityViolationException ex) {
        LOGGER.warn("Handle Database exception with error code:{}, message:{}", ServerErrorCode.DATABASE_EXCEPTION, ex.getCause().getCause().getMessage());

        return new ErrorResponse(ServerErrorCode.DATABASE_EXCEPTION.toString(),
                String.format("Server can not save your data. %s", ex.getCause().getCause().getMessage())
        );
    }

    //HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotReadableReq(HttpMessageNotReadableException ex) {
        LOGGER.warn("Handle Not Readable Message exception with error code:{}, message:{}", ServerErrorCode.NOT_READABLE_MESSAGE, ex.getCause().getCause().getMessage());

        return new ErrorResponse(ServerErrorCode.NOT_READABLE_MESSAGE.toString(),
                String.format("Server can not read your data. %s", ex.getCause().getCause().getMessage())
        );
    }


    public static class ErrorsResponse {
        private List<ErrorResponse> errorResponses = new ArrayList<>();

        public List<ErrorResponse> getErrorResponses() {
            return errorResponses;
        }

        public void setErrorResponses(List<ErrorResponse> errorResponses) {
            this.errorResponses = errorResponses;
        }
    }

    public static class ErrorResponse {

        private String errorCode;

        private String message;

        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}