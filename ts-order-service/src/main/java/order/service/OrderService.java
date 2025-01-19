package order.service;

import edu.fudan.common.entity.Seat;
import edu.fudan.common.util.Response;
import order.entity.*;
import org.springframework.http.HttpHeaders;

import java.util.Date;
import java.util.UUID;

/**
 * @author fdse
 */
public interface OrderService {

    Response findOrderById(UUID id, HttpHeaders headers);

    Response create(Order newOrder, HttpHeaders headers);

    Response saveChanges(Order order, HttpHeaders headers);

    Response cancelOrder(String accountId, UUID orderId, HttpHeaders headers);

    Response queryOrders(OrderInfo qi, String accountId, HttpHeaders headers);

    Response queryOrdersForRefresh(OrderInfo qi, String accountId, HttpHeaders headers);

    Response alterOrder(OrderAlterInfo oai, HttpHeaders headers);

    Response queryAlreadySoldOrders(Date travelDate, String trainNumber, HttpHeaders headers);

    Response getAllOrders(HttpHeaders headers);

    Response modifyOrder(UUID orderId, int status, HttpHeaders headers);

    Response getOrderPrice(UUID orderId, HttpHeaders headers);

    Response payOrder(UUID orderId, HttpHeaders headers);

    Response getOrderById(UUID orderId , HttpHeaders headers);

    Response checkSecurityAboutOrder(Date checkDate, String accountId, HttpHeaders headers);

    void initOrder(Order order, HttpHeaders headers);

    Response deleteOrder(UUID orderId, HttpHeaders headers);

    Response getSoldTickets(Seat seatRequest, HttpHeaders headers);

    Response addNewOrder(Order order, HttpHeaders headers);

    Response updateOrder(Order order, HttpHeaders headers);
}
