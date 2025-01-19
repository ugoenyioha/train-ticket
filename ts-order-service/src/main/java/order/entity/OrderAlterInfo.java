package order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import java.util.UUID;

/**
 * @author fdse
 */
@Data
@AllArgsConstructor
public class OrderAlterInfo {

    @Column(length = 36)
    private String accountId;

    @Column(length = 36)
    private UUID previousOrderId;

    private String loginToken;

    private Order newOrderInfo;

    public OrderAlterInfo(){
        newOrderInfo = new Order();
    }
}
