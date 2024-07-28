package gift.mapper;

import gift.DTO.OrderDTO;
import gift.entity.OrderEntity;
import gift.service.ProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderMapper {

    @Autowired
    private ProductOptionService productOptionService;

    public OrderDTO toOrderDTO(OrderEntity orderEntity) {
        return new OrderDTO(
                orderEntity.getId(),
                orderEntity.getProductOptionEntity().getId(),
                orderEntity.getQuantity(),
                orderEntity.getCreatedAt(),
                orderEntity.getMessage()
        );
    }

    public OrderEntity toOrderEntity(OrderDTO orderDTO, boolean idRequired) {
        Long id = null;
        if (idRequired) {
            id = orderDTO.id();
        }

        var orderEntity = new OrderEntity();
        orderEntity.setId(id);
        orderEntity.setProductOptionEntity(productOptionService.getProductOptionEntity(orderDTO.optionId()));
        orderEntity.setQuantity(orderDTO.quantity());
        orderEntity.setMessage(orderDTO.message());

        return orderEntity;
    }

}
