package gift.domain;

public record MemberRequest(
        String id,
        String name,
        String password
) {
}
