package other.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
public class OrderAlterInfo {

    @Id
    @Column(length = 36)
    private String accountId;

    @Column(length = 36)
    private String previousOrderId;

    private String loginToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "new_order_id")
    private Order newOrderInfo;

    public OrderAlterInfo(){
        newOrderInfo = new Order();
    }

    public String getOrderId(){
        return previousOrderId;
    }
}
