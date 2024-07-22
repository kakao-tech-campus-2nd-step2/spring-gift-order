package gift.controller.dto;

public class KakaoApiDTO {
    public record KakaoCode(String client_id,
                            String redirect_uri,
                            String response_type){}
    public record KakaoTokenRequset(String grant_type,
                                    String client_id,
                                    String redirect_uri,
                                    String code,
                                    String client_secret){}
}
