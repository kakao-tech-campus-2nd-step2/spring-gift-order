package gift.users.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);

    boolean existsByKakaoId(String kakaoId);

    User findByKakaoId(String kakaoId);
}
