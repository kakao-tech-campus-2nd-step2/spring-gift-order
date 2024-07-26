package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishRepositoryTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private Member member;

    @Mock
    private Product product;

    @InjectMocks
    private Wish wish;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        product = mock(Product.class);
    }

    @Test
    void testSaveAndFindById() {
        // Given
        Wish mockWish = mock(Wish.class);
        when(mockWish.getId()).thenReturn(1L);
        when(mockWish.getMember()).thenReturn(member);
        when(mockWish.getProduct()).thenReturn(product);

        when(wishRepository.save(any(Wish.class))).thenReturn(mockWish);
        when(wishRepository.findById(1L)).thenReturn(Optional.of(mockWish));

        // When
        Wish savedWish = wishRepository.save(mockWish);
        Optional<Wish> foundWish = wishRepository.findById(1L);

        // Then
        assertThat(foundWish).isPresent();
        assertThat(foundWish.get().getId()).isEqualTo(1L);
        assertThat(foundWish.get().getMember()).isEqualTo(member);
        assertThat(foundWish.get().getProduct()).isEqualTo(product);

        verify(wishRepository).save(mockWish);
        verify(wishRepository).findById(1L);
    }

    @Test
    void testFindByMember() {
        // Given
        Wish wish = new Wish(member, product);
        when(wishRepository.save(any(Wish.class))).thenReturn(wish);
        when(wishRepository.findByMember(any(Member.class))).thenReturn(List.of(wish));

        // When
        wishRepository.save(wish);
        var wishes = wishRepository.findByMember(member);

        // Then
        assertThat(wishes).hasSize(1);
        assertThat(wishes.get(0).getMember()).isEqualTo(member);

        verify(wishRepository).save(wish);
        verify(wishRepository).findByMember(member);
    }

    @Test
    void testDeleteByMemberAndProduct() {
        // Given
        Wish wish = new Wish(member, product);
        when(wishRepository.save(any(Wish.class))).thenReturn(wish);
        doNothing().when(wishRepository).deleteByMemberAndProduct(any(Member.class), any(Product.class));

        // When
        wishRepository.save(wish);
        wishRepository.deleteByMemberAndProduct(member, product);

        // Then
        verify(wishRepository).save(wish);
        verify(wishRepository).deleteByMemberAndProduct(member, product);
    }

    @Test
    void testFindByMemberAndProduct() {
        // Given
        Wish wish = new Wish(member, product);
        when(wishRepository.save(any(Wish.class))).thenReturn(wish);
        when(wishRepository.findByMemberAndProduct(any(Member.class), any(Product.class))).thenReturn(Optional.of(wish));

        // When
        wishRepository.save(wish);
        Optional<Wish> foundWish = wishRepository.findByMemberAndProduct(member, product);

        // Then
        assertThat(foundWish).isPresent();
        assertThat(foundWish.get().getMember()).isEqualTo(member);
        assertThat(foundWish.get().getProduct()).isEqualTo(product);

        verify(wishRepository).save(wish);
        verify(wishRepository).findByMemberAndProduct(member, product);
    }

    @Test
    void testExistsByMemberAndProduct() {
        // Given
        Wish wish = new Wish(member, product);
        when(wishRepository.save(any(Wish.class))).thenReturn(wish);
        when(wishRepository.existsByMemberAndProduct(any(Member.class), any(Product.class))).thenReturn(true);

        // When
        wishRepository.save(wish);
        boolean exists = wishRepository.existsByMemberAndProduct(member, product);

        // Then
        assertThat(exists).isTrue();

        verify(wishRepository).save(wish);
        verify(wishRepository).existsByMemberAndProduct(member, product);
    }
}