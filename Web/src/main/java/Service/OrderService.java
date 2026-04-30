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

    public boolean addOrder(int userId, double totalAmount, String note, String paymentMethod, String paymentStatus,Integer dis, Integer ship, Integer address_id, String shipping_type, double shipping_cost, String delivered_date, Cart cart) {
        int order_id = orderDao.addOrder(userId, totalAmount, note, dis, ship,paymentMethod,paymentStatus);
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

    public List<OrderView> getAllOrders() {
        return orderDao.getAllOrder();
    }

    public boolean updateOrder(int orderId, Double orderTotalPrice, String orderStatus) {
        return orderDao.updateOrder(orderId,orderTotalPrice,orderStatus);
    }

    public boolean deleteOrder(int id) {
        return orderDao.deleteOrder(id);
    }

    public List<OrderView> searchOrder(String q, String sortDate, String fromDate, String toDate, String statusFilter) {
        if (q != null && q.isBlank()) q = null;
        if (sortDate != null && sortDate.isBlank()) sortDate = null;
        if (fromDate != null && fromDate.isBlank()) fromDate = null;
        if (toDate != null && toDate.isBlank()) toDate = null;

        return orderDao.searchAndFilter(q, sortDate, fromDate, toDate, statusFilter);
    }
    public List<OrderItemsView> getOrderItemsByOrderId(int orderId){
        return orderDao.getOrderItemsByOrderId(orderId);
    }
    public void  updateOrderStatus(int orderId, String status) {
        orderDao.updateOrderStatus(orderId,status);
    }
    public void  updatePaymentStatus(int orderId, String paymentStatus) {
        orderDao.updateOrderStatus(orderId,paymentStatus);
    }

    public List<OrderView> getOrderOfUser(int id) {
        return orderDao.getOrderOfUser(id);
    }

    public int totalOrder(int uid) {
        return orderDao.totalOrder(uid);
    }
    public double totalAmountOrder(int uid) {
        return orderDao.totalAmountOrder(uid);
    }

    public boolean deleteOrderOfUser(int userId, int id) {
        return orderDao.deleteOrderOfUser(userId,id);
    }
}
