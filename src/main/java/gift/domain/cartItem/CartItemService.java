package gift.domain.cartItem;

import gift.domain.cartItem.dto.CartItemDTO;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.user.JpaUserRepository;
import gift.domain.user.User;
import gift.global.exception.cartItem.CartItemNotFoundException;
import gift.global.exception.product.ProductNotFoundException;
import gift.global.exception.user.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {

    private final JpaProductRepository productRepository;
    private final JpaCartItemRepository cartItemRepository;
    private final JpaUserRepository userRepository;

    public CartItemService(
        JpaProductRepository jpaProductRepository,
        JpaCartItemRepository jpaCartItemRepository,
        JpaUserRepository jpaUserRepository
    ) {
        this.userRepository = jpaUserRepository;
        this.cartItemRepository = jpaCartItemRepository;
        this.productRepository = jpaProductRepository;
    }

    /**
     * 장바구니에 상품 ID 추가
     */
    @Transactional
    public int addCartItem(Long userId, Long productId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        // 기존에 존재하면 update
        Optional<CartItem> findCartItem = cartItemRepository.findByUserIdAndProductId(userId,
            productId);
        if (findCartItem.isPresent()) {
            return findCartItem.get().addOneMore();
        }
        // 기존에 없었으면 new
        CartItem newCartItem = new CartItem(user, product);
        cartItemRepository.save(newCartItem);
        return newCartItem.getCount();
    }

    /**
     * 장바구니 상품 조회 - 페이징(매개변수별)
     */
    public List<CartItemDTO> getProductsInCartByUserIdAndPageAndSort(Long userId, int page,
        int size,
        Sort sort) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<CartItem> cartItemsPage = cartItemRepository.findAllByUserId(userId,
            pageRequest);

        List<CartItemDTO> cartItemDTOS = cartItemsPage.getContent().stream()
            .map(cartItem -> {
                return new CartItemDTO(cartItem);
            })
            .toList();

        // 새 Page 객체 생성
        return cartItemDTOS;
    }

    /**
     * 장바구니 상품 삭제`
     */
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * 장바구니 상품 수량 수정
     */
    @Transactional
    public int updateCartItem(Long cartItemId, int count) {
        CartItem findCartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        findCartItem.updateCount(count); // 수량 수정
        return count;
    }
}
