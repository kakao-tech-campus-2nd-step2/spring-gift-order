package gift.Model.Value;

import jakarta.persistence.Embeddable;

@Embeddable
public class Description {
    private String description;

    public Description(String description) {
        validateDescription(description);

        this.description = description;
    }

    private void validateDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("description의 값은 null이 올 수 없습니다");
    }


    public String getDescription() {
        return description;
    }
}
