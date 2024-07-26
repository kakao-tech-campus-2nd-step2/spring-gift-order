package gift.domain;

import java.util.Map;

public class KakaoAccount {
    private String id;

    public KakaoAccount(Map<String, Object> kakaoAccountMap) {
        if (kakaoAccountMap != null) {
            this.id = (String) kakaoAccountMap.get("id");
        }
    }

    public String getId() {
        return id;
    }

}
