package gift.option.dto;

import gift.option.domain.OptionCount;
import gift.option.domain.OptionName;

public record OrderRequestDto(Long optionId, OptionCount count, String message) {
}
