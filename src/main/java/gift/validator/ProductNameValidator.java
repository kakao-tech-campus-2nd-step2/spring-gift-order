package gift.validator;

import gift.exception.InvalidProductNameException;

public class ProductNameValidator {

    public static void validate(String name) {
        if (name.contains("카카오")) {
            throw new InvalidProductNameException("상품명에 '카카오'를 포함할 경우, 담당 MD에게 문의바랍니다.");
        }
    }
}