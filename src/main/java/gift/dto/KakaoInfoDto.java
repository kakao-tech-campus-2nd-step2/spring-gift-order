package gift.dto;

public class KakaoInfoDto {
    private Long id;
    private kakao_account kakaoAccount;

    public Long getId() {
        return id;
    }

    public kakao_account getKakao_account() {
        return kakaoAccount;
    }

    public static class kakao_account {
        private String email;
        public String getEmail() {
            return email;
        }
    }
}