package gift.main.dto;

public record KakaoProfileRequest(Long id, Properties properties) {

    public String nickname(){
        return properties().nickname;
    }

    public String idToString(){
        return id.toString();
    }
    record Properties(String nickname) {
    }

}
