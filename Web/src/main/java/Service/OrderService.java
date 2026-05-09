package Service;

import Cart.Cart;
import DTO.MyOrderDTO;
import DTO.OrderDetailDTO;
import dao.OrderDao;
import dao.OrderDetailDAO;
import dao.OrderItemDao;
import dao.ShippingDao;
import model.Address;
import model.OrderItemsView;
import model.OrderView;

import java.util.List;
import java.util.Map;

public class OrderService {
    OrderDao orderDao;
    AddressService addressService;
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
        this.addressService = new AddressService();
    }

    public boolean addOrder(int userId, double totalAmount, String note, String paymentMethod, String paymentStatus,Integer dis, Integer ship, Address address_id, String shipping_type, double shipping_cost, String delivered_date, Cart cart) {
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

    public static void main(String[] args) {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        OrderDetailDTO  orderDetailDTO = orderDetailDAO.findOrderDetailByOrderId(95);
        System.out.println(orderDetailDTO.toString());
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

    public boolean addOrder(Integer uid, double finalTotal, String nopaid, String receiverName, String receiverPhone, String province, String district, String ward, String specificAddress, String shipName, double shipFee, String deliveryRange, Map<Integer, Integer> productMap) {
        int order_id = orderDao.addOrder(uid, finalTotal, "", null, null,"COD",nopaid);
        if (order_id != -1) {
            bookService.updateQuantity(productMap);
            bookService.updateStockk(productMap);
            orderItemDao.insertOrderItems(order_id, productMap);
            notificationService.sendNoti(uid,"Đơn hàng mới ("+order_id+") do admin tạo","Shop đã tạo cho bạn 1 đơn hàng mới do trụ trặc");
            shippingDao.addShipping(order_id, receiverName,receiverPhone,province,district,ward,specificAddress, shipName, shipFee, deliveryRange);
        }

        return order_id != -1;
    }
}
