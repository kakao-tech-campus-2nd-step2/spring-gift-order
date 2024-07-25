package gift.exception;

public class MissingAuthorizationCodeException extends RuntimeException{
    public MissingAuthorizationCodeException(String message){
        super(message);
    }
}
