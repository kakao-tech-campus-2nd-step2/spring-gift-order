package gift.entity;

public class MessageResponseDTO {
    String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public MessageResponseDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
