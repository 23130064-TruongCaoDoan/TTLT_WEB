package Service;

import Cart.Cart;
import DTO.MyOrderDTO;
import DTO.OrderDetailDTO;
import dao.OrderDao;
import dao.OrderDetailDAO;
import dao.OrderItemDao;
import dao.ShippingDao;
import model.OrderItemsView;
import model.OrderView;

import java.util.List;

public class OrderService {
    OrderDao orderDao;
    ShippingDao shippingDao;
    NotificationService notificationService;
    OrderItemDao orderItemDao;
    OrderDetailDAO dao = new OrderDetailDAO();
    BookService bookService = new BookService();

    public OrderService() {
        this.orderDao = new OrderDao();
        this.shippingDao = new ShippingDao();
        this.notificationService = new NotificationService();
        this.orderItemDao = new OrderItemDao();
    }

    public boolean addOrder(int userId, double totalAmount, String note, String paymentMethod,Integer dis, Integer ship, Integer address_id, String shipping_type, double shipping_cost, String delivered_date, Cart cart) {
        int order_id = orderDao.addOrder(userId, totalAmount, note, dis, ship,paymentMethod);
        if (order_id != -1) {
            bookService.updateQuantity(cart);
            bookService.updateStock(cart);
            orderItemDao.insertOrderItems(order_id, cart);
            notificationService.sendNoti(userId, "Bạn đã đặt đơn hàng: " + order_id, "Các sản phẩm bạn đặt: " + cart.getProductNamesAsString());
            int ship_id = shippingDao.addShipping(order_id, address_id, shipping_type, shipping_cost, delivered_date);
        }

        return order_id != -1;
    }
    public List<MyOrderDTO> getMyOrders(int userId, String status) {
        return orderDao.findOrdersByUserId(userId,status);
    }

    public OrderDetailDTO getOrderDetail(int orderId) {
        return dao.findOrderDetailByOrderId(orderId);
    }

        public static void main(String[] args) {
        OrderService orderService = new OrderService();
        System.out.println(orderService.getOrderItemsByOrderId(48).size());
    }
    public List<OrderView> getAllOrders() {
        return orderDao.getAllOrder();
    }

    public boolean updateOrder(int orderId, Double orderTotalPrice, String orderStatus) {
        return orderDao.updateOrder(orderId,orderTotalPrice,orderStatus);
    }

    public boolean deleteOrder(int id) {
        return orderDao.deleteOrder(id);
    }

    public List<OrderView> searchOrder(String q, String sortDate) {
        if (q != null && q.isBlank()) q = null;
        if (sortDate != null && sortDate.isBlank()) sortDate = null;

        return orderDao.searchAndFilter(q, sortDate);
    }
    public List<OrderItemsView> getOrderItemsByOrderId(int orderId){
        return orderDao.getOrderItemsByOrderId(orderId);
    }
}
