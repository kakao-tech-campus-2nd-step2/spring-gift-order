package gift.product.validation;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.NO_PERMISSION;

import gift.product.exception.InvalidIdException;
import gift.product.exception.UnauthorizedException;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import gift.product.repository.ProductRepository;
import gift.product.repository.WishListRepository;
import gift.product.util.JwtUtil;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishListValidation {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public WishListValidation(
            WishListRepository wishListRepository,
            ProductRepository productRepository,
            JwtUtil jwtUtil
    ) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }

    public Wish registerValidation(String authorization, Long productId) {
<<<<<<< HEAD
        Member member = jwtUtil.identification(authorization);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
=======

        Member member = jwtUtil.identification(authorization);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));

>>>>>>> e44b601 (feat: init code)
        return new Wish(member, product);
    }

    public void deleteValidation(String authorization, Long id) {
        System.out.println("[WishListValidation] deleteValidation()");
<<<<<<< HEAD
        Member member = jwtUtil.identification(authorization);
        Wish wish = wishListRepository.findById(id)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
=======

        Member member = jwtUtil.identification(authorization);

        Wish wish = wishListRepository.findById(id)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));

>>>>>>> e44b601 (feat: init code)
        if(!Objects.equals(wish.getMember().getId(), member.getId()))
            throw new UnauthorizedException(NO_PERMISSION);
    }
}
