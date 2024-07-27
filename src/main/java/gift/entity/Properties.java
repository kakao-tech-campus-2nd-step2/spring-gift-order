package gift.entity;

public class Properties {
    private String clientId;
    private String redirectUri;

    public Properties() {
        this("https://developers.kakao.com/console/app/1109424/config/appKey 여기에서 RestAPI키받아서 넣기","http://localhost:8080");
    }

    public Properties(String clientId, String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

}
