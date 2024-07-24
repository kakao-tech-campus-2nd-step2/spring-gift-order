package gift.dto.response;

public record KakaoUserInfoResponse(
        long id,
        KakaoAccount kakao_account

) {
    public record KakaoAccount(
            String email
    ) {
    }
}
