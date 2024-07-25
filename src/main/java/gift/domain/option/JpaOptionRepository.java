package gift.domain.option;

import gift.domain.product.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOptionRepository extends JpaRepository<Option, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Option> findByIdForUpdate(Long id);

    List<Option> findAllByProductId(Long productId);

    List<Option> findAllByProduct(Product product);
}
