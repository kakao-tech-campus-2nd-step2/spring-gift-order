package gift.wishlist.repository;

import gift.product.entity.Product;
import gift.wishlist.entity.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    boolean existsByUserIdAndProduct(String userId, Product product);

    Page<WishList> findByUserId(String userId, Pageable pageable);
}
