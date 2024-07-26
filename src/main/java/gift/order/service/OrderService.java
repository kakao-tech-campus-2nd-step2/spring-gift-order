package gift.order.service;

import gift.order.OrderListResponseDto;
import gift.order.OrderResponseDto;
import gift.order.OrderServiceDto;
import gift.order.domain.Order;
import gift.order.exception.OrderNotFoundException;
import gift.order.repository.OrderRepository;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public OrderListResponseDto getAllOrders() {
        return OrderListResponseDto.orderListToOptionListResponseDto(orderRepository.findAll());
    }

    public OrderResponseDto getOrderById(Long id) {
        return OrderResponseDto.orderToOrderResponseDto(orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new));
    }

    public Order createOrder(OrderServiceDto orderServiceDto) {
        Product product = productService.getProductById(orderServiceDto.productId());
        return orderRepository.save(orderServiceDto.toOrder(product));
    }

    public Order updateOrder(OrderServiceDto orderServiceDto) {
        validateOrderExists(orderServiceDto.id());
        Product product = productService.getProductById(orderServiceDto.productId());
        return orderRepository.save(orderServiceDto.toOrder(product));
    }

    public void deleteOrder(Long id) {
        validateOrderExists(id);
        orderRepository.deleteById(id);
    }

    private void validateOrderExists(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
    }
}
