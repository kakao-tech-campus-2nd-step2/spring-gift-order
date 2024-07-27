package gift.member.validator;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.member.entity.Member;

public class KakaoUserValidator {

    private static final String KAKAO_EMAIL_PREFIX = "kakao_user";
    private static final String KAKAO_EMAIL_SUFFIX = "@kakao.com";

    public static void validate(Member member) {
        if (member.isKakaoTokenInfoNotNull() && validateKakaoUserEmail(member.getEmail())) {
            return;
        }
        throw new CustomException(ErrorCode.MEMBER_NOT_KAKAO_USER);
    }

    private static boolean validateKakaoUserEmail(String email) {
        return email.startsWith(KAKAO_EMAIL_PREFIX) && email.endsWith(KAKAO_EMAIL_SUFFIX);
    }

}
