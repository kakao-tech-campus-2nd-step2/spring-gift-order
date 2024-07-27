package gift.Service;

import gift.DTO.RequestOrderDTO;
import gift.DTO.ResponseOrderDTO;
import gift.Exception.OptionNotFoundException;
import gift.Exception.OrderNotFoundException;
import gift.Model.Entity.*;
import gift.Repository.OptionRepository;
import gift.Repository.OrderRepository;
import gift.Repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OptionService optionService;
    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;

    public OrderService(OptionService optionService, WishRepository wishRepository, OptionRepository optionRepository, OrderRepository orderRepository) {
        this.optionService = optionService;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public ResponseOrderDTO createOrder(Member member, RequestOrderDTO requestOrderDTO) {
        Option option = optionRepository.findById(requestOrderDTO.optionId()).orElseThrow(()->
                new OptionNotFoundException("매칭되는 옵션이 없습니다"));

        optionService.subtractQuantity(option.getId(), requestOrderDTO.quantity());

        List<Wish> wishList = wishRepository.findWishListByMember(member);
        wishList.stream()
                .filter(it->it.getProduct().equals(option.getProduct()))
                .findFirst()
                .ifPresent(wish->wishRepository.deleteById(wish.getId()));

        Order order = orderRepository.save(
                new Order(option, member, requestOrderDTO.quantity(), LocalDateTime.now(), requestOrderDTO.message())
        );

        return ResponseOrderDTO.of(order);
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderDTO> getOrders(Member member){
       return orderRepository.findAllByMember(member)
               .stream()
               .map(ResponseOrderDTO::of)
               .toList();
    }

    @Transactional
    public void editOrder(Member member, Long orderId, Integer quantity){
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new OrderNotFoundException("해당하는 주문을 찾을 수 없습니다"));
        order.checkOrderBelongsToMember(member);
        order.updateQuantity(quantity);

    }

    @Transactional
    public void deleteOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당하는 주문을 찾을 수 없습니다"));
        order.checkOrderBelongsToMember(member);
        orderRepository.deleteById(orderId);
    }
}
