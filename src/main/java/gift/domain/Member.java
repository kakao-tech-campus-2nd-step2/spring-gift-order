package gift.domain;

import gift.domain.base.BaseTimeEntity;
import gift.domain.vo.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Member extends BaseTimeEntity {

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String name;

    protected Member() {
    }

    public static class Builder extends BaseTimeEntity.Builder<Builder> {

        private Email email;
        private String name;

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Member build() {
            return new Member(this);
        }
    }

    private Member(Builder builder) {
        super(builder);
        email = builder.email;
        name = builder.name;
    }

    public Email getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
