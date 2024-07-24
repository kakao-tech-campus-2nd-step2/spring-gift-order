package gift.auth.domain;

import gift.entity.enums.SocialType;

public class JWT {

    private Long id;
    private String email;
    private String socialToken = "";
    private SocialType socialType = SocialType.OTHER;

    public JWT(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public JWT(Long id, String email, String socialToken, SocialType socialType) {
        this.id = id;
        this.email = email;
        this.socialToken = socialToken;
        this.socialType = socialType;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public SocialType getSocialType() {
        return socialType;
    }
}
