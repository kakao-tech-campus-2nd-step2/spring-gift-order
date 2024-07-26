package gift.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TextObject {

    private String objectType;
    private String text;
    private Link link;

    public TextObject(String objectType, String text, Link link) {
        this.objectType = objectType;
        this.text = text;
        this.link = link;
    }
}
