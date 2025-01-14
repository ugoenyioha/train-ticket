package edu.fudan.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.fudan.common.entity.SeatClass;
import edu.fudan.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * @author fdse
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Order {

    private String id;

    private String boughtDate;

    private String travelDate;

    private String travelTime;

    /**
     * Which Account Bought it
     */
    private String accountId;

    /**
     * Tickets bought for whom....
     */
    private String contactsName;

    private int documentType;

    private String contactsDocumentNumber;

    private String trainNumber;

    private int coachNumber;

    private int seatClass;

    private int seatNumber;

    private String from;

    private String to;

    private int status;

    private String price;

    private String differenceMoney;

    public Order(){
        boughtDate = StringUtils.Date2String(new Date(System.currentTimeMillis()));
        travelDate = StringUtils.Date2String(new Date(123456789));
        trainNumber = "G1235";
        coachNumber = 5;
        seatClass = SeatClass.FIRSTCLASS.getCode();
        seatNumber = 2;
        from = "shanghai";
        to = "taiyuan";
        status = OrderStatus.PAID.getCode();
        price = "0.0";
        differenceMoney ="0.0";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Order other = (Order) obj;
        return this.boughtDate.equals(other.boughtDate)
                && this.boughtDate.equals(other.travelDate)
                && this.travelTime.equals(other.travelTime)
                && this.accountId.equals(other.accountId)
                && this.contactsName.equals(other.contactsName)
                && this.contactsDocumentNumber.equals(other.contactsDocumentNumber)
                && this.documentType == other.documentType
                && this.trainNumber.equals(other.trainNumber)
                && this.coachNumber == other.coachNumber
                && this.seatClass == other.seatClass
                && this.seatNumber == other.seatNumber
                && this.from.equals(other.from)
                && this.to.equals(other.to)
                && this.status == other.status
                && this.price.equals(other.price);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.hashCode());
        return result;
    }

}
