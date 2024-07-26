package gift.entity;

public class Properties {
    private String clientId;
    private String redirectUri;
    private String authorizationCode;



    public Properties() {
        this("51e91e613f587d79dc0bfa02e3054f95","http://localhost:8080");
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

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
