package gift.users.kakao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record KakaoOrderDTO(@NotNull(message = "상품 아이디를 입력하지 않았습니다.")
                            long productId,
                            @NotNull(message = "옵션 아이디를 입력하지 않았습니다.")
                            long optionId,
                            @NotNull(message = "상품 수량을 입력하지 않았습니다.")
                            @Min(value = 1, message = "상품은 한 개부터 구매 가능합니다.")
                            int quantity,
                            String orderDateTime,
                            String message) {

}
