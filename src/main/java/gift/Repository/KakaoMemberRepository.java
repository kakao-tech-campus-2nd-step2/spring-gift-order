package gift.Repository;

import gift.DTO.KakaoMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember, String> {

}
