package gift.domain.user.dto.kakao;

public record KaKaoToken(
    String accessToken,
    String refreshToken
) {

}
