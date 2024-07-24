package gift.RepositoryTest;


import gift.domain.*;
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

    @Test
    @DisplayName("위시리스트 저장 테스트")
    void testSaveWishList() {
        Member member = new Member("member1", "password1",new LinkedList<WishList>());
        member = memberRepository.save(member);

        Category category = new Category(null,"양식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option1 = new Option(null,"알리오올리오",3L);
        Option option2 = new Option(null,"토마토",4L);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Set<Option> options = new HashSet<Option>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타",3000,"naver.com",category,options);

        WishList wishList = new WishList(member, menu);
        wishList = wishListRepository.save(wishList);

        assertThat(wishList.getId()).isNotNull();
    }

    @Test
    @DisplayName("위시리스트 FindById 테스트")
    void testFindWishListById() {
        Member member = new Member("member1", "password1",new LinkedList<WishList>());
        member = memberRepository.save(member);

        Category category = new Category(null,"양식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option1 = new Option(null,"알리오올리오",3L);
        Option option2 = new Option(null,"토마토",4L);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Set<Option> options = new HashSet<Option>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타",3000,"naver.com",category,options);

        WishList wishList = new WishList(member, menu);
        wishList = wishListRepository.save(wishList);

        Optional<WishList> foundWishList = wishListRepository.findById(wishList.getId());
        assertThat(foundWishList).isPresent();
        assertThat(foundWishList.get().getMenu().getName()).isEqualTo("파스타");
    }

    @Test
    @DisplayName("위시리스트 삭제 테스트")
    void testDeleteWishList() {
        Member member = new Member("member1", "password1",new LinkedList<WishList>());
        member = memberRepository.save(member);

        Category category = new Category(null,"양식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option1 = new Option(null,"알리오올리오",3L);
        Option option2 = new Option(null,"토마토",4L);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Set<Option> options = new HashSet<Option>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타",3000,"naver.com",category,options);

        WishList wishList = new WishList(member, menu);
        wishList = wishListRepository.save(wishList);

        wishListRepository.deleteById(wishList.getId());

        Optional<WishList> deletedWishList = wishListRepository.findById(wishList.getId());
        assertThat(deletedWishList).isNotPresent();
    }
}
