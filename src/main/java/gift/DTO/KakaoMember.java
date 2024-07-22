package gift.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class KakaoMember {

  @Id
  private String clientId;
  private String code;

  protected KakaoMember() {
  }

  public KakaoMember(String clientId, String code) {
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
