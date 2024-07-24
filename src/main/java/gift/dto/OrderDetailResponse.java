package gift.dto;

import gift.entity.Option;
import gift.entity.User;
import java.time.LocalDateTime;

public record OrderDetailResponse(Long id, Option option, User user, Integer quantity,
                                  LocalDateTime localDateTime, String message) {

}
