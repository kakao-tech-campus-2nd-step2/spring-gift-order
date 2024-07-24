package gift.user;

public class KakaoUser {
    String accessToken;
    User user;

    public KakaoUser(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }
}
