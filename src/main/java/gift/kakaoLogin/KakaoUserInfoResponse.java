package gift.kakaoLogin;

public class KakaoUserInfoResponse {
    Long id;
    String connected_at;

    public KakaoUserInfoResponse(Long id, String connected_at) {
        this.id = id;
        this.connected_at = connected_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnected_at() {
        return connected_at;
    }

    public void setConnected_at(String connected_at) {
        this.connected_at = connected_at;
    }
}
