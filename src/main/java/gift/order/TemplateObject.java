package gift.order;

public class TemplateObject {
    String object_type;
    String text;
    Link link;

    public TemplateObject(String object_type, String text, Link link) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
    }
}
