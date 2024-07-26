package gift.repository;

public class Properties {
    private String clientId;
    private String redirectUri;
    private String authorizationCode;

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

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
