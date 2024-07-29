package gift.RepositoryTest;

import gift.domain.menu.Menu;
import gift.domain.other.Category;
import gift.domain.other.Member;
import gift.domain.other.Option;
import gift.domain.other.WishList;
import gift.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Member member;
    private Category category;
    private Option option1;
    private Option option2;
    private Set<Option> options;
    private Menu menu;

    @BeforeEach
    void setUp() {
        // Clean up repositories
        wishListRepository.deleteAll();
        optionRepository.deleteAll();
        menuRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();

        // Initialize common entities
        member = new Member("member1", "password1", "김민지", new LinkedList<WishList>());
        member = memberRepository.save(member);

        category = new Category(null, "양식", new LinkedList<Menu>());
        category = categoryRepository.save(category);

        option1 = new Option(null, "알리오올리오", 3L,menu);
        option2 = new Option(null, "토마토", 4L,menu);
        option1 = optionRepository.save(option1);
        option2 = optionRepository.save(option2);

        options = new HashSet<>();
        options.add(option1);
        options.add(option2);

        menu = new Menu("파스타", 3000, "naver.com", category, options);
        menu = menuRepository.save(menu);
    }

    @Test
    @DisplayName("위시리스트 저장 테스트")
    void testSaveWishList() {
        WishList wishList = new WishList(member, menu);
        wishList = wishListRepository.save(wishList);

        assertThat(wishList.getId()).isNotNull();
    }

    @Test
    @DisplayName("위시리스트 FindById 테스트")
    void testFindWishListById() {
        WishList wishList = new WishList(member, menu);
        wishList = wishListRepository.save(wishList);

        Optional<WishList> foundWishList = wishListRepository.findById(wishList.getId());
        assertThat(foundWishList).isPresent();
        assertThat(foundWishList.get().getMenu().getName()).isEqualTo("파스타");
    }

    @Test
    @DisplayName("위시리스트 삭제 테스트")
    void testDeleteWishList() {
        WishList wishList = new WishList(member, menu);
        wishList = wishListRepository.save(wishList);

        wishListRepository.deleteById(wishList.getId());

        Optional<WishList> deletedWishList = wishListRepository.findById(wishList.getId());
        assertThat(deletedWishList).isNotPresent();
    }
}
