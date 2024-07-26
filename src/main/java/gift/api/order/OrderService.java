package gift.api.order;

import gift.api.member.MemberDao;
import gift.api.option.OptionDao;
import gift.api.option.OptionService;
import gift.api.order.dto.MsgMeResponse;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.order.dto.TemplateObject;
import gift.api.wishlist.WishService;
import gift.api.wishlist.dto.WishDeleteRequest;
import gift.global.config.KakaoProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberDao memberDao;
    private final OptionService optionService;
    private final OptionDao optionDao;
    private final WishService wishService;
    private final KakaoProperties properties;
    private final RestClient restClient;

    public OrderService(OrderRepository orderRepository, MemberDao memberDao, OptionService optionService,
        OptionDao optionDao, WishService wishService, KakaoProperties properties) {

        this.orderRepository = orderRepository;
        this.memberDao = memberDao;
        this.optionService = optionService;
        this.optionDao = optionDao;
        this.wishService = wishService;
        this.properties = properties;
        restClient = RestClient.create();
    }

    public OrderResponse order(Long memberId, OrderRequest orderRequest) {
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());
        wishService.delete(memberId,
            WishDeleteRequest.of(optionDao.findProductIdById(orderRequest.optionId())));
        Order order = orderRepository.save(
            orderRequest.toEntity(optionDao.findOptionById(orderRequest.optionId()),
                orderRequest.message()));
        return OrderResponse.of(order.getId(),
                                order.getOption().getId(),
                                order.getOption().getQuantity(),
                                order.getOrderDateTime(),
                                order.getMessage());
    }

    public void sendMessage(Long memberId, OrderRequest orderRequest) {
        MsgMeResponse msgMeResponse = restClient.post()
            .uri(properties.url().defaultTemplateMsgMe())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + memberDao.findMemberById(memberId).getKakaoAccessToken())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(createBody(orderRequest))
            .retrieve()
            .body(MsgMeResponse.class);

        if (msgMeResponse.resultCode() != 0) {
            throw new MessageFailException();
        }
    }

    private LinkedMultiValueMap<Object, Object> createBody(OrderRequest orderRequest) {
        var body = new LinkedMultiValueMap<>();
        body.add("template_object", TemplateObject.of(orderRequest.message(), "", "", "바로 확인"));
        return body;
    }
}
