package gift.DTO;

public class KakaoJwtToken {

  private String accessToken;

  private String tokenType;
  private String refreshToken;
  private int expiresIn;
  private String scope;
  private int refreshTokenExpiresIn;

  public KakaoJwtToken(String accessToken, String tokenType, String refreshToken, int expiresIn,
    String scope, int refreshTokenExpiresIn) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.scope = scope;
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;
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
