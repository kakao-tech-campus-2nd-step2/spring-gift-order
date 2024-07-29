package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.wish.Wish;
import gift.domain.wish.WishRepository;
import gift.dto.OrderRequestDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.infra.TemplateObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;

@Service
public class OrderService {
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository, MemberRepository memberRepository, ObjectMapper objectMapper) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void handleOrder(Long memberId, OrderRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
        //1. 옵션 수량 차감
        Option option = subtractOption(request.optionId(), request.quantity());
        //2. 위시 리스트 삭제
        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId());
        wish.ifPresent(wishRepository::delete);

        //3. 주문 내역 전송
        sendMessage(member.getAccessToken());
    }

    private Option subtractOption(Long optionId, Integer quantity) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_OPTION));
        option.subtract(quantity);
        return option;
    }

    private void sendMessage(String accessToken) {
        String uri = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        TemplateObject templateObject = new TemplateObject("text", "상품 주문",
                new TemplateObject.Link("https://gift.kakao.com/home", "https://gift.kakao.com/home"));
        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RestClient restClient = RestClient.builder().build();
        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", templateObjectJson);
        System.out.println(templateObjectJson);
        var response = restClient.post()
                .uri(URI.create(uri))
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        System.out.println(response.getBody());
    }
}
