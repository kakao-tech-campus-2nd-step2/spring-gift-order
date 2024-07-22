package gift.DTO;

public class KakaoMemberDto {

  private String clientId;
  private String code;

  protected KakaoMemberDto() {
  }

  public KakaoMemberDto(String clientId, String code) {
    this.clientId = clientId;
    this.code = code;
  }

  public String getClientId() {
    return this.clientId;
  }

  public String getCode() {
    return this.code;
  }
}
