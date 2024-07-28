package gift.order;

public record MessageTemplate(
    String object_type,
    String text,
    Link link
) { }
