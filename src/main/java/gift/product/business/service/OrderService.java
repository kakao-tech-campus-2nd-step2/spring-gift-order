package gift.product.business.service;

import gift.member.business.service.WishlistService;
import gift.member.persistence.repository.MemberRepository;
import gift.product.business.dto.OptionIn;
import gift.product.business.dto.OrderIn;
import gift.product.persistence.repository.OrderRepository;
import gift.product.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public OrderService(ProductService productService, WishlistService wishlistService,
        OrderRepository orderRepository, ProductRepository productRepository,
        MemberRepository memberRepository) {
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long createOrder(OrderIn.Create orderInCreate) {
        var optionInSubtract = new OptionIn.Subtract(
            orderInCreate.optionId(),
            orderInCreate.quantity()
        );
        productService.subtractOption(optionInSubtract, orderInCreate.productId());

        try {
            wishlistService.deleteWishList(orderInCreate.memberId(), orderInCreate.productId());
        } catch (Exception ignored) {
        }

        var product = productRepository.getReferencedProduct(orderInCreate.productId());
        var member = memberRepository.getReferencedMember(orderInCreate.memberId());
        var order = orderInCreate.toOrder(product, member);
        var orderId = orderRepository.saveOrder(order);

        //TODO: Send message using Kakaotalk API

        return orderId;
    }
}
