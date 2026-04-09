package dao;

import DTO.OrderDetailDTO;
import DTO.OrderItemDTO;
import Service.AddressService;
import Service.CommentService;
import model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderDetailDAO extends BaseDao{
    public OrderDetailDTO findOrderDetailByOrderId(int orderId) {

        return getJdbi().withHandle(handle -> {

            Order order = handle.createQuery("""
                SELECT id, user_id, order_date, status,
                       total_amount, note, payment_method,
                       dis_voucher_id, ship_voucher_id
                FROM orders
                WHERE id = :orderId
            """)
                    .bind("orderId", orderId)
                    .mapToBean(Order.class)
                    .one();

            // Shipping + Address (JOIN)
            Map<String, Object> shipAddr = handle.createQuery("""
                SELECT s.order_id, s.shipping_type, s.shipping_cost,
                       s.shipping_date, s.delivered_date, s.status,
                       a.id, a.name, a.phone, a.city, a.ward,
                       a.specificAddress, a.id AS address_id
                FROM shipping s
                JOIN address a ON s.address_id = a.id
                WHERE s.order_id = :orderId
            """)
                    .bind("orderId", orderId)
                    .mapToMap()
                    .one();

            Shipping shipping = new Shipping();
            shipping.setOrderId(orderId);
            shipping.setShippingType((String) shipAddr.get("shipping_type"));
            BigDecimal cost = (BigDecimal) shipAddr.get("shipping_cost");
            shipping.setShippingCost(cost == null ? null : cost.doubleValue());
            shipping.setShippingDate((Date) shipAddr.get("shipping_date"));
            shipping.setDeliveredDate((String) shipAddr.get("delivered_date"));
            shipping.setStatus((String) shipAddr.get("status"));


            AddressService  addressService = new AddressService();
            Address address = addressService.getAddressById((Integer) shipAddr.get("address_id"));
            // Order items + Book (JOIN)
            List<OrderItemDTO> items = handle.createQuery("""
                SELECT oi.book_id,
                       b.title,
                       b.type,
                       b.cover_img_url,
                       b.price_discounted,
                       oi.quantity,
                       oi.subtotal
                FROM order_items oi
                JOIN books b ON oi.book_id = b.id
                WHERE oi.order_id = :orderId
            """)
                    .bind("orderId", orderId)
                    .mapToBean(OrderItemDTO.class)
                    .list();
            // Gộp DTO
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setOrder(order);
            dto.setShipping(shipping);
            dto.setAddress(address);
            dto.setItems(items);

            return dto;
        });
    }

    public static void main(String[] args) {
        OrderDetailDAO dao = new OrderDetailDAO();
        System.out.println(dao.findOrderDetailByOrderId(43));
    }

}
