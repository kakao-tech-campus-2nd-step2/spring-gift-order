package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Util.JWTUtil;
import gift.dto.option.*;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.exception.exception.ServerInternalException;
import gift.exception.exception.UnAuthException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OptionService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    JWTUtil jwtUtil;
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    UserRepository userRepository;

    @Transactional
    public void refill(OptionQuantityDTO optionQuantityDTO) {
        Option option = optionRepository.findById(optionQuantityDTO.optionId()).orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
        option.addQuantity(optionQuantityDTO.quantity());

    }

    @Transactional
    public OrderResponseDTO order(OptionQuantityDTO optionQuantityDTO, String token) {
        Option option = optionRepository.findById(optionQuantityDTO.optionId()).orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
        if (option.getQuantity() < optionQuantityDTO.quantity())
            throw new BadRequestException("재고보다 많은 물건 주문 불가능");
        option.subQuantity(optionQuantityDTO.quantity());
        User user = userRepository.findById(jwtUtil.getUserIdFromToken(token)).orElseThrow(()->new NotFoundException("유저 없음"));
        String kakaoToken = jwtUtil.getKakaoTokenFromToken(token);
        Order order = new Order(optionQuantityDTO, option, user);
        user.addOrder(order);

        if(kakaoToken!=null)
            sendMessage(order, kakaoToken);

        order = orderRepository.save(order);
        return order.toResponseDTO();
    }

    private void sendMessage(Order order, String token)  {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", order.getMessage());

        Map<String, String> link = new HashMap<>();
        link.put("web_url", "https://developers.kakao.com");
        link.put("mobile_web_url", "https://developers.kakao.com");
        templateObject.put("link", link);

        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new ServerInternalException("파싱 에러");
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED)
            throw new UnAuthException("인증되지 않은 요청");
        if (response.getStatusCode() != HttpStatus.OK)
            throw new BadRequestException("잘못된 요청");
    }

    public void add(SaveOptionDTO saveOptionDTO) {
        Product product = productRepository.findById(saveOptionDTO.product_id()).orElseThrow(() -> new NotFoundException("해당 물품이 없음"));
        optionRepository.findByOption(saveOptionDTO.option()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 옵션");
        });
        Option option = new Option(product, saveOptionDTO.option());
        optionRepository.save(option);
    }

    public void delete(int id) {
        Option option = optionRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
        Product product = option.getProduct();
        product.deleteOption(option);
        optionRepository.deleteById(id);
    }

    @Transactional
    public void update(UpdateOptionDTO updateOptionDTO) {
        Option option = optionRepository.findById(updateOptionDTO.id()).orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
        optionRepository.findByOption(updateOptionDTO.option()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 옵션");
        });
        option.changeOption(updateOptionDTO.option());
    }

}
