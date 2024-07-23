package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Color {

    @Column(nullable = false)
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

    @Override
    public boolean equals(Object object){
        if (this== object)
            return true;

        if(!(object instanceof Color))
            return false;

        Color color = (Color) object;
        return Objects.equals(this.color, color.getColor());
    }

    @Override
    public int hashCode(){
        return Objects.hash(color);
    }
}
