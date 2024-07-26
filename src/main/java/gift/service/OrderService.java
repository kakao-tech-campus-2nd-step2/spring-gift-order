package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.CreateOrderDto;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ProductService productService;
    private final OptionService optionService;
    private final WishService wishService;

    public OrderService(OrderRepository orderRepository, MemberService memberService, ProductService productService, OptionService optionService, WishService wishService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.productService = productService;
        this.optionService = optionService;
        this.wishService = wishService;
    }

    public Order createOrder(CreateOrderDto orderDto) {
        Member member = memberService.getMemberById(orderDto.getMemberId());
        Product product = productService.getProduct(orderDto.getProductId());
        Option option = optionService.getOption(orderDto.getOptionId());

        optionService.decreaseQuantity(orderDto.getOptionId(), orderDto.getQuantity());

        wishService.deleteWish(product);

        Order order = new Order(member, product, option, orderDto.getQuantity(), orderDto.getMessage());
        return orderRepository.save(order);
    }
}
