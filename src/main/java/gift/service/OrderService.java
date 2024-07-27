package gift.service;

import gift.entity.*;
import gift.exception.ResourceNotFoundException;
import gift.repository.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final RestClient client = RestClient.builder().build();

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionService optionService;
    private final ProductWishlistRepository productWishlistRepository;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public OrderService(UserRepository userRepository, WishlistRepository wishlistRepository, OrderRepository orderRepository, ProductOptionRepository productOptionRepository, OptionService optionService, ProductWishlistRepository productWishlistRepository, ProductService productService, UserService userService) {
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.optionService = optionService;
        this.productWishlistRepository = productWishlistRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Order save(String email, OrderDTO orderDTO) {
        ProductOption result = productOptionRepository
                .findByProductIdAndOptionId(orderDTO.getProductId(), orderDTO.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Illegal order"));

        User user = userService.findOne(email);

        // 해당 옵션 수량 차감
        Option option = result.getOption();
        optionService.subtract(option.getId(), orderDTO.getQuantity());

        // 위시리스트에 있으면 삭제
        Optional<Wishlist> wishlist = wishlistRepository.findByEmail(email);
        if (wishlist.isPresent()) {
            productWishlistRepository.deleteByProductIdAndWishlistId(orderDTO.getProductId(), wishlist.get().getId());
        }

        Order order = new Order(orderDTO);
        order.setUser(user);

        return orderRepository.save(order);
    }

    public List<Order> findAll(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUserId(user.getId());
    }

    public void delete(Long orderId, String email) {
        User user = userService.findOne(email);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (user.getId() != order.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        orderRepository.delete(order);
    }

    public void sendToMe(String kakaoAccessToken, OrderDTO order) {
        LinkedMultiValueMap<String, String> body = makeRequestBody(order);

        String response = client.post()
                .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    private LinkedMultiValueMap<String, String> makeRequestBody(OrderDTO order) {
        String redirect_url = "http://localhost:8080/me";
        Product product = productService.findById(order.getProductId());
        String msg = product.getName() + " : " + Integer.toString(order.getQuantity()) + "\n" + order.getMessage();

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", redirect_url);
        linkObj.put("mobile_web_url", redirect_url);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("object_type", "text");
        jsonObj.put("text", msg);
        jsonObj.put("link", linkObj);

        body.add("template_object", jsonObj.toString());
        return body;
    }
}
