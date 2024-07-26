package gift.web.client.dto;

public class KakaoAccount {

    private KakaoProfile profile;
    private String email;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;

    public KakaoAccount(KakaoProfile profile, String email, Boolean isEmailValid,
        Boolean isEmailVerified) {
        this.profile = profile;
        this.email = email;
        this.isEmailValid = isEmailValid;
        this.isEmailVerified = isEmailVerified;
    }

    public KakaoProfile getProfile() {
        return profile;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEmailValid() {
        return isEmailValid;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }
}
