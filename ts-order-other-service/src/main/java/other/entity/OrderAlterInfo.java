package other.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderAlterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;

    @Column(name = "new_order_id")
    private String newOrderId;

    @Column(name = "new_order_info")
    private Order newOrderInfo;

    // ... rest of the class
}
