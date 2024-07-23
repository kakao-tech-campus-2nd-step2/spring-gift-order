package gift.users.wishlist;

import gift.administrator.option.Option;
import gift.administrator.option.OptionDTO;
import gift.administrator.option.OptionService;
import gift.administrator.product.Product;
import gift.administrator.product.ProductDTO;
import gift.administrator.product.ProductService;
import gift.users.user.User;
import gift.users.user.UserDTO;
import gift.users.user.UserService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OptionService optionService;
    private final JwtUtil jwtUtil;

    public WishListService(WishListRepository wishListRepository, ProductService productService,
        UserService userService, OptionService optionService,
        JwtUtil jwtUtil) {
        this.wishListRepository = wishListRepository;
        this.productService = productService;
        this.userService = userService;
        this.optionService = optionService;
        this.jwtUtil = jwtUtil;
    }

    public Page<WishListDTO> getWishListsByUserId(long id, int page, int size, Direction direction,
        String sortBy) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageRequest = PageRequest.of(page, size, sort);
        Page<WishList> wishListPage = wishListRepository.findAllByUserId(id, pageRequest);
        List<WishListDTO> wishLists = wishListPage.stream()
            .map(WishListDTO::fromWishList)
            .toList();
        return new PageImpl<>(wishLists, pageRequest, wishListPage.getTotalElements());
    }

    public void extractUserIdFromTokenAndValidate(HttpServletRequest request, Long userId) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String token = authHeader.substring(7);
        String tokenUserId = jwtUtil.extractUserId(token);
        if (!userId.toString().equals(tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유저 아이디가 토큰과 일치하지 않습니다.");
        }
    }

    public WishListDTO addWishList(WishListDTO wishListDTO, Long userId) throws NotFoundException {
        UserDTO userDTO = userService.findById(userId);
        User user = userDTO.toUser();
        validateIfProductExists(userId, wishListDTO.getProductId(), user.getEmail());
        validateOptionId(wishListDTO.getProductId(), wishListDTO.getOptionId());

        Product product = toProductByProductIdAndSetOption(wishListDTO.getProductId());

        OptionDTO optionDTO = optionService.findOptionById(wishListDTO.getOptionId());
        Option option = optionDTO.toOption(product);

        WishList wishList = new WishList(user, product, wishListDTO.getNum(), option);
        user.addWishList(wishList);
        product.addWishList(wishList);
        wishListRepository.save(wishList);
        return WishListDTO.fromWishList(wishList);
    }

    public void validateOptionId(long productId, long optionId) {
        if (!optionService.existsByOptionIdAndProductId(optionId,
            productId)) {
            throw new IllegalArgumentException(
                optionId + " 옵션은 " + productId + " 상품에 존재하지 않는 옵션입니다.");
        }
    }

    public WishListDTO updateWishList(long userId, long productId, WishListDTO wishListDTO)
        throws NotFoundException {
        validateIfProductNotExists(userId, productId);

        WishList wishList = wishListRepository.findByUserIdAndProductId(userId, productId);
        validateOptionId(productId, wishListDTO.getOptionId());

        Product product = toProductByProductIdAndSetOption(productId);

        OptionDTO newOptionDTO = optionService.findOptionById(wishListDTO.getOptionId());
        Option newOption = newOptionDTO.toOption(product);

        wishList.setOption(newOption);
        wishList.setNum(wishListDTO.getNum());
        newOption.addWishList(wishList);
        wishListRepository.save(wishList);
        return WishListDTO.fromWishList(wishList);
    }

    private Product toProductByProductIdAndSetOption(long productId) throws NotFoundException {
        ProductDTO productDTO = productService.getProductById(productId);
        Product product = productDTO.toProduct(productDTO,
            productService.getCategoryById(productDTO.getCategoryId()));
        product.setOption(optionService.getAllOptionsByOptionId(
                productDTO.getOptions().stream().map(OptionDTO::getId).toList()).stream()
            .map(optionDTO -> optionDTO.toOption(product)).toList());
        return product;
    }

    private void validateIfProductNotExists(long userId, long productId) throws NotFoundException {
        if (!wishListRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new IllegalArgumentException(
                userService.findById(userId).email() + "의 위시리스트에는 " + productService.getProductById(
                    productId).getName()
                    + " 상품이 존재하지 않습니다.");
        }
    }

    private void validateIfProductExists(long userId, long productId, String email){
        if (wishListRepository.existsByUserIdAndProductId(userId,
            productId)) {
            throw new IllegalArgumentException(email + "의 위시리스트에 존재하는 상품입니다.");
        }
    }

    @Transactional
    public void deleteWishList(long userId, long productId) throws NotFoundException {
        if (!wishListRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new IllegalArgumentException(
                userService.findById(userId).email() + "의 위시리스트에는 " + productService.getProductById(
                    productId).getName()
                    + " 상품이 존재하지 않습니다.");
        }
        WishList wishList = wishListRepository.findByUserIdAndProductId(userId, productId);
        wishList.getOption().removeWishList(wishList);
        wishList.getUser().removeWishList(wishList);
        wishList.getProduct().removeWishList(wishList);
        wishListRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
