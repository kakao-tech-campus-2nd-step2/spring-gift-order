package gift.main.dto;

public record KakaoProfile(Long id, Properties properties) {

    public String nickname(){
        return properties().nickname;
    }

    public String idToString(){
        return id.toString();
    }
    record Properties(String nickname) {
    }

}
