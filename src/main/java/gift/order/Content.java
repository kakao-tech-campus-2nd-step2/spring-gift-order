package gift.order;

public class Content {
    String title;
    String description;
    String image_url;
    Link link;


    public Content(String title, String description, String image_url, Link link) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.link = link;
    }

}
