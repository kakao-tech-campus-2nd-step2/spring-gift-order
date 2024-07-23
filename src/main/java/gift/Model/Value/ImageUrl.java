package gift.Model.Value;

import jakarta.persistence.Embeddable;

@Embeddable
public class ImageUrl {
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
}
