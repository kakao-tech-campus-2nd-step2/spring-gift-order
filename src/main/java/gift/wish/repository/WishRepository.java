package gift.wish.repository;

import gift.global.MyCrudRepository;
import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.wish.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends MyCrudRepository<Wish, Long> {
    List<Wish> findAllByMemberId(Long memberId);

    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    void deleteByMemberAndProduct(@Param("member") Member member, @Param("product") Product product);
}
