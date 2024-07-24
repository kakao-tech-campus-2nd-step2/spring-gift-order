package gift.DTO.Kakao;

public class Content {
    private String title;
    private String image_url;
    private String description;
    private Link link;

    public Content(String title, String image_url, String description, Link link) {
        this.title = title;
        this.image_url = image_url;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDescription() {
        return description;
    }

    public Link getLink() {
        return link;
    }
}
