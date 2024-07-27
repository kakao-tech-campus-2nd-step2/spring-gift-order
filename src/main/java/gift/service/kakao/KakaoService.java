package gift.service.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.kakao.KakaoProperties;
import gift.domain.order.OrderRequest;
import gift.domain.product.option.ProductOption;
import gift.domain.user.User;
import gift.repository.product.option.ProductOptionRepository;
import gift.repository.wish.WishRepository;
import gift.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import gift.util.JwtTokenUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {

    private final RestClient client;
    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;
    private final UserService userService;
    private final ProductOptionRepository productOptionRepository;
    private final WishRepository wishRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public KakaoService(KakaoProperties kakaoProperties,
                        UserService userService,
                        ProductOptionRepository productOptionRepository,
                        WishRepository wishRepository,
                        JwtTokenUtil jwtTokenUtil) {
        this.client = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.productOptionRepository = productOptionRepository;
        this.wishRepository = wishRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    public Map<String, Object> kakaoLogin(String authorizationCode) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String clientId = kakaoProperties.clientId();
        String redirectUri = kakaoProperties.redirectUri();

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        try {
            var tokenResponse = requestAccessToken(tokenUrl, body);
            String accessToken = extractAccessToken(tokenResponse);

            Map<String, Object> userInfo = getUserInfo(accessToken);
            userInfo.put("access_token", accessToken);

            // 사용자 정보 조회 및 생성 로직 추가
            Long id = (Long) userInfo.get("id");
            String email = (String) userInfo.get("email");
            User user = userService.findOrCreateUser(id, email);
            // JWT 토큰을 생성하여 반환
            String jwtToken = jwtTokenUtil.generateAccessToken(user.getEmail());
            userInfo.put("jwt_token", jwtToken);
            String jwtRefresh = jwtTokenUtil.generateRefreshToken(user.getEmail());
            userInfo.put("jwt_refresh", jwtRefresh);

            // 필요하다면 user 객체의 정보를 userInfo에 추가
            //userInfo.put("user_id", user.getId());
            //userInfo.put("user_email", user.getEmail());

            return userInfo;

        } catch (RestClientException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    private String requestAccessToken(String url, LinkedMultiValueMap<String, String> body) {
        try {
            var response = client.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(String.class);
            return response;
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to request access token", e);
        }
    }

    private String extractAccessToken(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract access token", e);
        }
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        Map<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            String response = client.post()
                    .uri(URI.create(reqURL))
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            JsonNode jsonNode = objectMapper.readTree(response);

            long id = jsonNode.path("id").asLong();
            JsonNode properties = jsonNode.path("properties");
            String nickname = properties.path("nickname").asText();
            JsonNode kakaoAccount = jsonNode.path("kakao_account");
            String email = kakaoAccount.path("email").asText();

            userInfo.put("id", id);
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to get user info", e);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get user info", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userInfo;
    }


    @Transactional
    public Map<String, Object> createOrder(User user, String accessToken, OrderRequest orderRequest) {
        ProductOption productOption = productOptionRepository.findById(orderRequest.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        // Subtract quantity from product option
        productOption.subtract(orderRequest.getQuantity());
        productOptionRepository.save(productOption);

        // Delete wish if exists
        wishRepository.findByUserIdAndProductIdAndIsDeletedFalse(user.getId(), productOption.getProduct().getId())
                .ifPresent(wish -> {
                    wish.setIsDeleted(true);
                    wishRepository.save(wish);
                });

        // Send Kakao message
        sendKakaoMessage(user, accessToken, orderRequest.getMessage(), productOption.getName(), orderRequest.getQuantity());

        // Return response
        Map<String, Object> response = new HashMap<>();
        response.put("id", 1);  // This should be replaced with the actual order ID
        response.put("optionId", orderRequest.getOptionId());
        response.put("quantity", orderRequest.getQuantity());
        response.put("orderDateTime", "2024-07-21T10:00:00");  // This should be replaced with the actual order date/time
        response.put("message", orderRequest.getMessage());

        return response;
    }

    private void sendKakaoMessage(User user, String accessToken, String message, String optionName, Long quantity) {
        String reqURL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", createTemplateObject(user, message, optionName, quantity));

        try {
            client.post()
                    .uri(URI.create(reqURL))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to send Kakao message", e);
        }
    }

    private String createTemplateObject(User user, String message, String optionName, Long quantity) {
        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", String.format("Order received: %s\nOption: %s\nQuantity: %d\nMessage: %s",
                user.getEmail(), optionName, quantity, message));
        templateObject.put("link", Map.of("web_url", "http://your-web-url.com"));
        //"web_url" 키에 해당하는 웹 URL을 지정하여 사용자가 메시지에서 클릭할 수 있는 링크를 제공

        try {
            return objectMapper.writeValueAsString(templateObject);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create template object", e);
        }
    }
}
