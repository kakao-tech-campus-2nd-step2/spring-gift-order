package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.awt.*;
import java.util.Objects;

@Embeddable
public class ImageUrl {

    @Column(nullable = false)
    private String imageUrl;

    public ImageUrl(String imageUrl) {
        validateImageUrl(imageUrl);

        this.imageUrl = imageUrl;
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank())
            throw new IllegalArgumentException("imageUrl를 입력해주세요");

    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof ImageUrl))
            return false;

        ImageUrl imageUrl = (ImageUrl) object;
        return Objects.equals(this.imageUrl, imageUrl.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
