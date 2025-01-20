package foodsearch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Delivery {
    public Delivery() {
        //Default Constructor
    }

    private String orderId;
    private String foodName;
    private String storeName;
    private String stationName;
}
