package mail_app.exception;

public class ServerException extends RuntimeException {
    private ServerErrorCode errorCode;

    public ServerException(String errorMessage, ServerErrorCode errorCode){
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public ServerErrorCode getErrorCode(){
        return errorCode;
    }
}
