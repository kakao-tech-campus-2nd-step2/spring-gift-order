package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Properties {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private final String clientId;
    private final String redirectUri;

    public Properties() {
        this("5c345cd75534a877a4c4e77e6e7bd288", "http://localhost:8080");
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
