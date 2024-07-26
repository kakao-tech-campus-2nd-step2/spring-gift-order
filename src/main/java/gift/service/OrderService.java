package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.request.OrderRequest;
import gift.exception.CustomException;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static gift.exception.ErrorCode.DATA_NOT_FOUND;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
    }

    public void order(Member member, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId()).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        option.subtract(orderRequest.quantity());
        deleteFromWishList(member, option.getProduct());
    }

    private void deleteFromWishList(Member member, Product product) {
        Optional<Wish> wish = wishRepository.findWishByMemberAndProduct(member, product);
        wish.ifPresent(wishRepository::delete);
    }
}
