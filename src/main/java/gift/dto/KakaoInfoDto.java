package gift.dto;

public class KakaoInfoDto {
    private Long id;
    private kakao_account kakao_Account;

    public KakaoInfoDto(Long id, kakao_account kakao_Account){
        this.id = id;
        this. kakao_Account = kakao_Account;
    }

    public Long getId() {
        return id;
    }

    public kakao_account getKakao_account() {
        return kakao_Account;
    }

    public static class kakao_account {
        private String email;
        
        public kakao_account(String  email){
            this.email = email;
        }
        public String getEmail() {
            return email;
        }
    }
}