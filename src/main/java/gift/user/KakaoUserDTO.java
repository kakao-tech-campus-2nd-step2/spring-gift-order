package gift.user;

public class KakaoUserDTO {
    UserDTO userDTO;
    String accessToken;

    public KakaoUserDTO(UserDTO userDTO, String accessToken) {
        this.userDTO = userDTO;
        this.accessToken = accessToken;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
