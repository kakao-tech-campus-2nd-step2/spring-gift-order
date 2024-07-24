package gift.DTO.Kakao;

public class Template {
    private String object_type;
    private Content content;
    private Commerce commerce;

    public Template(String object_type, Content content, Commerce commerce) {
        this.object_type = object_type;
        this.content = content;
        this.commerce = commerce;
    }

    public String getObject_type() {
        return object_type;
    }

    public Content getContent() {
        return content;
    }

    public Commerce getCommerce() {
        return commerce;
    }
}
