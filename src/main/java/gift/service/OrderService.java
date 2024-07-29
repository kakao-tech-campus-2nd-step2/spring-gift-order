package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.constants.ErrorMessage;
import gift.domain.KakaoMessageSender;
import gift.dto.KakaoCommerceMessage;
import gift.dto.OptionSubtractRequest;
import gift.dto.OrderRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.KakaoTokenRepository;
import gift.repository.MemberJpaDao;
import gift.repository.ProductJpaDao;
import gift.repository.WishlistJpaDao;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductJpaDao productJpaDao;
    private final MemberJpaDao memberJpaDao;
    private final WishlistJpaDao wishlistJpaDao;

    private final OptionService optionService;

    private final KakaoMessageSender kakaoMessageSender;

    public OrderService(ProductJpaDao productJpaDao, MemberJpaDao memberJpaDao,
        WishlistJpaDao wishlistJpaDao, OptionService optionService,
        KakaoMessageSender kakaoMessageSender) {
        this.productJpaDao = productJpaDao;
        this.memberJpaDao = memberJpaDao;
        this.wishlistJpaDao = wishlistJpaDao;
        this.optionService = optionService;
        this.kakaoMessageSender = kakaoMessageSender;
    }

    /**
     * 메모리에 저장된 카카오 토큰, 회원, 상품의 존재 여부를 확인 후 메시지 전송. 만약 위시 리스트에 추가된 상품이라면 위시 리스트에서 삭제.
     */
    @Transactional
    public void order(OrderRequest orderRequest, String email) throws JsonProcessingException {
        Member member = findMember(email);
        Product product = findProduct(orderRequest.getProductId());

        sendMessageForMe(product, orderRequest, email);
        optionService.subtractOption(new OptionSubtractRequest(orderRequest));

        if (member.containsWish(product.getId())) {
            wishlistJpaDao.deleteByMember_EmailAndProduct_Id(email, product.getId());
        }
    }

    private void sendMessageForMe(Product product, OrderRequest orderRequest, String email)
        throws JsonProcessingException {
        String kakaoToken = KakaoTokenRepository.getAccessToken(email);
        kakaoMessageSender.sendForMe(kakaoToken,
            new KakaoCommerceMessage(product.getName(), product.getImageUrl(),
                orderRequest.getMessage(),
                product.getImageUrl(), (int) product.getPrice())
        );
    }

    private Member findMember(String email) {
        return memberJpaDao.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.MEMBER_NOT_EXISTS_MSG));
    }

    private Product findProduct(Long productId) {
        return productJpaDao.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));
    }
}
