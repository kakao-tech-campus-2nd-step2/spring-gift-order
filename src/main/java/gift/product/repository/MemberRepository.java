package gift.product.repository;

import gift.product.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
<<<<<<< HEAD
    boolean existsByEmail(String email);
=======
>>>>>>> e44b601 (feat: init code)
}
