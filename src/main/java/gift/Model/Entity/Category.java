package gift.Model.Entity;


import gift.Model.Value.Color;
import gift.Model.Value.Description;
import gift.Model.Value.ImageUrl;
import gift.Model.Value.Name;
import jakarta.persistence.*;

@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private Name name;

    @Embedded
    private Color color;

    @Embedded
    private ImageUrl imageUrl;

    @Embedded
    private Description description;


    protected Category (){}

    public Category(Name name, Color color, ImageUrl imageUrl, Description description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = new Name(name);
        this.color = new Color(color);
        this.imageUrl = new ImageUrl(imageUrl);
        this.description = new Description(description);
    }


    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public Description getDescription() {
        return description;
    }

    public void update(Name name, Color color, ImageUrl imageUrl, Description description){
        this.name = name;
        this.color = color;
        this. imageUrl = imageUrl;
        this.description = description;
    }

    public void update(String name, String color, String imageUrl, String description) {
        update(new Name(name), new Color(color), new ImageUrl(imageUrl), new Description(description));
    }
}
