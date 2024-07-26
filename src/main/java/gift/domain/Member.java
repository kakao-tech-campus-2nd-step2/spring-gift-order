package gift.domain;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "member")
public class Member {
    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<WishList> wishList;

    public Member() {
    }

    public Member(String id, String password, String name,List<WishList> wishList) {
        this.id = id;
        this.password = password;
        this.wishList = wishList;
        this.name = name;
    }


    public String getPassword() {
        return password;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public List<WishList> getWishList() {
        return wishList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
