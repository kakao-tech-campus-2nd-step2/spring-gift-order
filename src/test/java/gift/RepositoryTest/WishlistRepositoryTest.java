package gift.RepositoryTest;

import gift.Entity.*;
import gift.Mapper.Mapper;
import gift.Model.MemberDto;
import gift.Model.OptionDto;
import gift.Model.ProductDto;
import gift.Model.WishlistDto;
import gift.Repository.*;
import gift.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WishlistRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private WishlistJpaRepository wishlistJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        ProductService productService = Mockito.mock(ProductService.class);
        MemberService memberService = Mockito.mock(MemberService.class);
        WishlistService wishlistService = Mockito.mock(WishlistService.class);
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        OptionService optionService = Mockito.mock(OptionService.class);

        mapper = new Mapper(productService, memberService, wishlistService, categoryService, optionService);
    }

    @Test
    public void testGetWishlist() {
        MemberDto member1 = new MemberDto(1L, "1234@naver.com", "1234", "1234", false);
        Member member = mapper.memberDtoToEntity(member1);
        memberJpaRepository.save(member);

        OptionDto optionDto = new OptionDto(1L, 1L, "option1", 1000, 1);
        Option option = mapper.optionDtoToEntity(optionDto);
        optionJpaRepository.save(option);

        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 1, 0, "test", 1000, 1L);
        Wishlist wishlist = mapper.wishlistDtoToEntity(wishlistDto);
        Wishlist savedwishlist = wishlistJpaRepository.save(wishlist);

        assertThat(savedwishlist.getMemberId()).isEqualTo(wishlist.getMemberId());
        assertThat(savedwishlist.getProductId()).isEqualTo(wishlist.getProductId());
        assertThat(savedwishlist.getCount()).isEqualTo(wishlist.getCount());
        assertThat(savedwishlist.getPrice()).isEqualTo(wishlist.getPrice());
        assertThat(savedwishlist.getProductName()).isEqualTo(wishlist.getProductName());
    }

    @Test
    public void testAddWishlist() {
        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 1, 0, "test", 1000, 1L);
        Wishlist wishlist = mapper.wishlistDtoToEntity(wishlistDto);
        Wishlist savedwishlist = wishlistJpaRepository.save(wishlist);

        assertThat(savedwishlist.getMemberId()).isEqualTo(wishlist.getMemberId());
        assertThat(savedwishlist.getProductId()).isEqualTo(wishlist.getProductId());
        assertThat(savedwishlist.getCount()).isEqualTo(wishlist.getCount());
        assertThat(savedwishlist.getPrice()).isEqualTo(wishlist.getPrice());
        assertThat(savedwishlist.getProductName()).isEqualTo(wishlist.getProductName());
    }

    @Test
    @DisplayName("delete가 정상적으로 이루어지는지")
    public void testRemoveWishlist() {
        MemberDto member1 = new MemberDto(1L, "1234@naver.com", "1234", "1234", false);
        Member member = mapper.memberDtoToEntity(member1);
        memberJpaRepository.save(member);

        Category category = new Category(1L, "category1");
        categoryJpaRepository.save(category);

        ProductDto productDto1 = new ProductDto(1L, "productDto1", 1L, 1000, "http://localhost:8080/image1.jpg", false);
        Product product = mapper.productDtoToEntity(productDto1);
        productJpaRepository.save(product);

        OptionDto optionDto = new OptionDto(1L, 1L, "option1", 1000, 1);
        Option option = mapper.optionDtoToEntity(optionDto);
        optionJpaRepository.save(option);

        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 5, 0, "productDto1", 1000, 1L);
        Wishlist wishlist = mapper.wishlistDtoToEntity(wishlistDto);
        Wishlist savedwishlist = wishlistJpaRepository.save(wishlist);

        wishlistJpaRepository.delete(savedwishlist);

        Optional<Wishlist> foundWishlist = wishlistJpaRepository.findByWishlistId(savedwishlist.getMemberId(), savedwishlist.getProductId());

        assertThat(foundWishlist).isEmpty();

    }

    @Test
    @DisplayName("Update가 정상적으로 이루어지는지")
    public void testUpdateWishlistItem() {
        MemberDto member1 = new MemberDto(1L, "1234@naver.com", "1234", "1234", false);
        Member member = mapper.memberDtoToEntity(member1);
        memberJpaRepository.save(member);

        Category category = new Category(1L, "category1");
        categoryJpaRepository.save(category);

        ProductDto productDto1 = new ProductDto(1L, "productDto1", 1L, 1000, "http://localhost:8080/image1.jpg", false);
        Product product = mapper.productDtoToEntity(productDto1);
        productJpaRepository.save(product);

        OptionDto optionDto = new OptionDto(1L, 1L, "option1", 1000, 1);
        Option option = mapper.optionDtoToEntity(optionDto);
        optionJpaRepository.save(option);

        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 5, 0, "productDto1", 5000, 1L);
        Wishlist wishlist = mapper.wishlistDtoToEntity(wishlistDto);
        wishlistJpaRepository.save(wishlist);

        //수량을 5개에서 3개로 변경
        WishlistDto updateDto = new WishlistDto(1L, 1L, 3, 0, "test", 3000, 1L);
        Wishlist updateWishlist = mapper.wishlistDtoToEntity(updateDto);

        wishlistJpaRepository.save(updateWishlist);

        Optional<Wishlist> foundWishlistOptional = wishlistJpaRepository.findByWishlistId(updateWishlist.getMemberId(), updateWishlist.getProductId());
        Wishlist foundWishlist = foundWishlistOptional.get();
        assertThat(foundWishlist.getMemberId()).isEqualTo(updateWishlist.getMemberId());
        assertThat(foundWishlist.getProductId()).isEqualTo(updateWishlist.getProductId());
        assertThat(foundWishlist.getCount()).isEqualTo(updateWishlist.getCount());
        assertThat(foundWishlist.getProductName()).isEqualTo(updateWishlist.getProductName());
        assertThat(foundWishlist.getPrice()).isEqualTo(updateWishlist.getPrice());

    }
}
