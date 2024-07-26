package gift.service;

import gift.dto.WishRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<Wish> getWishes(Pageable pageable) {
        Pageable limitedPageable = PageRequest.of(pageable.getPageNumber(), Math.min(pageable.getPageSize(), 5));
        return wishRepository.findAll(limitedPageable);
    }

    public List<Wish> getWishes(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return wishRepository.findByMember(member);
    }

    public Wish addWish(String email, WishRequest wishRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        Product product = productRepository.findById(wishRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        if (wishRepository.existsByMemberAndProduct(member, product)) {
            throw new RuntimeException("위시 리스트에 이미 존재하는 제품입니다.");
        }

        Wish wish = new Wish(member, product);
        return wishRepository.save(wish);
    }

    public void removeWish(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        wishRepository.deleteByMemberAndProduct(member, product);
    }
}