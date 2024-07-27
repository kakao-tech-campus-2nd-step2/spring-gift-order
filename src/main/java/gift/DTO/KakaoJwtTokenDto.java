package gift.DTO;

public class KakaoJwtTokenDto {

  private final String accessToken;
  private final String tokenType;
  private final String refreshToken;
  private final int expiresIn;
  private final String scope;
  private final int refreshTokenExpiresIn;
  private Long id;

  public KakaoJwtTokenDto(String accessToken, String tokenType, String refreshToken, int expiresIn,
    String scope, int refreshTokenExpiresIn) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.scope = scope;
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;
  }

  public KakaoJwtTokenDto(Long id, String accessToken, String tokenType, String refreshToken,
    int expiresIn,
    String scope, int refreshTokenExpiresIn) {
    this.id = id;
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.scope = scope;
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;
  }

  public Long getId() {
    return this.id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public String getScope() {
    return scope;
  }

  public int getRefreshTokenExpiresIn() {
    return refreshTokenExpiresIn;
  }

}
