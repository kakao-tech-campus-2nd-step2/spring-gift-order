package gift.Service;

import gift.DTO.KakaoJwtToken;
import gift.DTO.OptionDto;
import gift.DTO.OrderDto;
import gift.DTO.Product;
import gift.DTO.ProductDto;
import gift.DTO.WishList;
import gift.Repository.KakaoJwtTokenRepository;
import gift.Repository.ProductRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

  private OptionService optionService;
  private ProductRepository productRepository;
  private WishListService wishListService;
  private KakaoJwtTokenRepository kakaoJwtTokenRepository;
  private RestClient restClient = RestClient.builder().build();

  private RestTemplate restTemplate;

  private final String URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

  public OrderService(OptionService optionService, ProductRepository productRepository,
    WishListService wishListService, KakaoJwtTokenRepository kakaoJwtTokenRepository) {
    this.optionService = optionService;
    this.productRepository = productRepository;
    this.wishListService = wishListService;
    this.kakaoJwtTokenRepository = kakaoJwtTokenRepository;
  }

  public void orderOption(OrderDto orderDto) throws IllegalAccessException {
    restTemplate=new RestTemplate();
    OptionDto optionDto = orderDto.getOptionDto();
    optionService.optionQuantitySubtract(optionDto, orderDto.getQuantity());

    ProductDto productDto = optionDto.getProductDto();
    Product product = productRepository.findById(productDto.getId())
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 상품이 없습니다", 1));
    List<WishList> wishLists = product.getWishlists();
    for (WishList wishList : wishLists) {
      wishListService.deleteProductToWishList(wishList.getId());
    }

    KakaoJwtToken kakaoJwtToken = kakaoJwtTokenRepository.findById(1L)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));

    System.out.println(kakaoJwtToken.getAccessToken());

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    headers.setBearerAuth(kakaoJwtToken.getAccessToken());

    String body = "template_object="+createTemplateObject();
    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);


    System.out.println(response);

  }

  private String createTemplateObject() {
    return """
      {
      "object_type" : "feed",
      "content" : {
        "title" : "주문이 완료되었습니다.",
        "image_url" : "imageUrl",
        "description" : "상세설명입니다.",
        "link" : {
          }
        } 
      }
      """;
  }
}
