package gift.Service;

import gift.DTO.OptionDto;
import gift.DTO.OrderDto;
import gift.DTO.Product;
import gift.DTO.ProductDto;
import gift.DTO.WishList;
import gift.Repository.ProductRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

  private OptionService optionService;
  private ProductRepository productRepository;
  private WishListService wishListService;
  private RestClient restClient;

  public OrderService(OptionService optionService, ProductRepository productRepository,
    WishListService wishListService) {
    this.optionService = optionService;
    this.productRepository = productRepository;
    this.wishListService = wishListService;
  }

  public void orderOption(OrderDto orderDto) throws IllegalAccessException {
    OptionDto optionDto = orderDto.getOptionDto();
    optionService.optionQuantitySubtract(optionDto, orderDto.getQuantity());

    ProductDto productDto = optionDto.getProductDto();
    Product product = productRepository.findById(productDto.getId())
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 상품이 없습니다", 1));
    List<WishList> wishLists = product.getWishlists();
    for (WishList wishList : wishLists) {
      wishListService.deleteProductToWishList(wishList.getId());
    }
  }
}
