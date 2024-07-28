package gift.value;

public class KakaoString {

    private String value;

    public KakaoString(String value) {
        this.value = value;
    }

    public String removeNewlines() {
        if (this.value == null) {
            return null;
        }
        return this.value.replaceAll("[\\r\\n]", "");
    }

    public String getValue() {
        return value;
    }
}
