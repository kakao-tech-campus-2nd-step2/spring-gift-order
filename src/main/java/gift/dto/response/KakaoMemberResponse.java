package gift.dto.response;

import gift.domain.KakaoMember;

public record KakaoMemberResponse(String email) {
    public static KakaoMemberResponse from(final KakaoMember kakaoMember){
        return new KakaoMemberResponse(kakaoMember.getEmail());
    }
}
