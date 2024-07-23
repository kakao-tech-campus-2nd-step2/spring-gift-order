package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class Name {

    private String name;

    public Name(String name) {
        validateName(name);

        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("이름 값은 필수입니다");

    }

    public void checkNameLength(int length){
        if (length <= 0 )
            throw new IllegalArgumentException("이름의 길이값이 0일수는 없습니다");

        if (name.length() > length){
            throw new IllegalArgumentException("이름의 길이가 "+length+"를 초과합니다");
        }
    }

    public void checkNamePattern(Pattern pattern){
        if (pattern == null)
            throw new IllegalArgumentException("검사하려는 패턴이 null입니다");
        if (!pattern.matcher(name).matches())
            throw new IllegalArgumentException("이름에 허용되지 않은 패턴이 들어있습니다");
    }

    public boolean isSame(String name){
        return this.name.equals(name);
    }

    public String getName() {
        return name;
    }
}
