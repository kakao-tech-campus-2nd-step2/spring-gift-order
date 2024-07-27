package gift.member.validator;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.member.entity.Member;

public class KakaoUserValidator {

    public static void validate(Member member) {
        if (member.isKakaoTokenInfoNull()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_KAKAO_USER);
        }
    }

}
