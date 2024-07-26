package gift.member.business.dto;

import gift.member.persistence.entity.Member;

public class MemberIn {

    public record Login(
        String email,
        String password
    ) {

    }

    public record Register(
        String email,
        String password
    ) {

        public Member toMember() {
            return new Member(email, password);
        }
    }
}
