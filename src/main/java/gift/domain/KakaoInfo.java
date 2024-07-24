package gift.domain;

public class KakaoInfo {
    private final Long id;
    private final String email;

    public KakaoInfo(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

}
