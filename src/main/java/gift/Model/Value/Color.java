package gift.Model.Value;

import jakarta.persistence.Embeddable;

@Embeddable
public class Color {
    private String color;

    public Color(String color){
        validateColor(color);

        this.color = color;
    }

    private void validateColor(String color){
        if (color == null || color.isBlank())
            throw new IllegalArgumentException("색상 값은 필수입니다");

        if (color.length() != 7)
            throw new IllegalArgumentException(" 색상 값의 길이는 7입니다");
    }

    public String getColor() {
        return color;
    }
}
