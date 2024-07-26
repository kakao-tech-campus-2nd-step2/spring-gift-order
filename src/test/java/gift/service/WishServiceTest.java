package gift.service;

import gift.dto.WishRequest;
import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWishesPageable() {
        // Given
        Wish wish = mock(Wish.class);
        Page<Wish> page = new PageImpl<>(Collections.singletonList(wish));
        Pageable pageable = PageRequest.of(0, 5);

        when(wishRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Wish> result = wishService.getWishes(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(wishRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetWishesByEmail() {
        // Given
        Member member = new Member("test@example.com", "password");
        Wish wish = new Wish(member, new Product("Test Product", 100, "http://example.com/image.jpg", new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description")));

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(wishRepository.findByMember(any(Member.class))).thenReturn(Collections.singletonList(wish));

        // When
        List<Wish> result = wishService.getWishes("test@example.com");

        // Then
        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(wishRepository, times(1)).findByMember(any(Member.class));
    }

    @Test
    public void testAddWish() {
        // Given
        Member member = new Member("test@example.com", "password");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description"));
        WishRequest wishRequest = new WishRequest(1L);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(wishRepository.existsByMemberAndProduct(any(Member.class), any(Product.class))).thenReturn(false);
        when(wishRepository.save(any(Wish.class))).thenReturn(new Wish(member, product));

        // When
        Wish result = wishService.addWish("test@example.com", wishRequest);

        // Then
        assertNotNull(result);
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(productRepository, times(1)).findById(anyLong());
        verify(wishRepository, times(1)).existsByMemberAndProduct(any(Member.class), any(Product.class));
        verify(wishRepository, times(1)).save(any(Wish.class));
    }

    @Test
    public void testRemoveWish() {
        // Given
        Member member = new Member("test@example.com", "password");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description"));

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(wishRepository).deleteByMemberAndProduct(any(Member.class), any(Product.class));

        // When
        wishService.removeWish("test@example.com", 1L);

        // Then
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(productRepository, times(1)).findById(anyLong());
        verify(wishRepository, times(1)).deleteByMemberAndProduct(any(Member.class), any(Product.class));
    }
}