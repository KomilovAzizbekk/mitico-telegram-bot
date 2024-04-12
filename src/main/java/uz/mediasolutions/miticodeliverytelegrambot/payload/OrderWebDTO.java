package uz.mediasolutions.miticodeliverytelegrambot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderWebDTO {

    private Long id;

    private BranchWebDTO branchWebDTO;

    private String status;

    private Long userId;

    private Double lon;

    private Double lat;

    private List<OrderProductResDTO> orderProducts;

    private String paymentProviderName;

    private String comment;

    private float paidSum;

    private float price;

    private float deliveryPrice;

    private float totalPrice;

    private String createdAt;

}
