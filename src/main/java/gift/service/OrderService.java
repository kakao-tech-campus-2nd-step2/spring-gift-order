package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.*;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private KakaoService kakaoService;
    private OptionRepository optionRepository;
    private MemberRepository memberRepository;
    private OrderRepository orderRepository;

    public OrderService(KakaoService kakaoService,OptionRepository optionRepository,MemberRepository memberRepository,OrderRepository orderRepository){
        this.kakaoService = kakaoService;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }

    public Order order(String token, OrderRequest orderRequest) throws IllegalAccessException, JsonProcessingException {
        Option option = optionRepository.getById(orderRequest.optionId());
        option.subtract(orderRequest.quantity());
        optionRepository.save(option);

        Menu menu = option.getMenu();

        Member member = kakaoService.getUserInformation(token.replace("Bearer ",""));
        List<WishList> wishListList = member.getWishList();
        wishListList.removeIf(wishList -> wishList.getMenu().equals(menu));
        member.setWishList(wishListList);
        memberRepository.save(member);

        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization",token);

        Map<String, Object> link = new HashMap<>();
        link.put("web_url", "https://developers.kakao.com");
        link.put("mobile_web_url", "https://developers.kakao.com");

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", orderRequest.message());
        templateObject.put("link", link);
        templateObject.put("button_title", "바로 확인");

        String templateObjectJson = new ObjectMapper().writeValueAsString(templateObject);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);


        Date now = new Date();

        Order order = new Order(null,orderRequest.optionId(),orderRequest.quantity(),now,orderRequest.message());

        order = orderRepository.save(order);
        return order;
    }
}
