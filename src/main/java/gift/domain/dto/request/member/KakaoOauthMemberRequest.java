package gift.domain.dto.request.member;

import gift.domain.entity.KakaoOauthMember;
import gift.domain.entity.Member;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import jakarta.validation.constraints.NotNull;

public class KakaoOauthMemberRequest extends MemberRequest {

    @NotNull
    protected final String kakaoAccessToken;

    public KakaoOauthMemberRequest(String email, String kakaoAccessToken) {
        super(Type.KAKAO, email);
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public KakaoOauthMember toEntity(Member member, Integer kakaoId) {
        return new KakaoOauthMember(kakaoId, member);
    }
}
