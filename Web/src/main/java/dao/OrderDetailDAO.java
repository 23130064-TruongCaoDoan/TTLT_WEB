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
                SELECT id,
                       user_id,
                       order_date,
                       status,
                       total_amount,
                       note,
                       payment_method,
                       payment_status,
                       dis_voucher_id,
                       ship_voucher_id
                FROM orders
                WHERE id = :orderId
        """)
                    .bind("orderId", orderId)
                    .mapToBean(Order.class)
                    .findOne()
                    .orElse(null);

            if (order == null) {
                return null;
            }

            Shipping shipping = handle.createQuery("""
                SELECT order_id,
                       shipping_type,
                       shipping_cost,
                       shipping_date,
                       delivered_date,
                       status,
                       receiver,
                       ward,
                       districts,
                       city,
                       specificAddress,
                       phone
                FROM shipping
                WHERE order_id = :orderId
        """)
                    .bind("orderId", orderId)
                    .mapToBean(Shipping.class)
                    .findOne()
                    .orElse(null);

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

            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setOrder(order);
            dto.setShipping(shipping);
            dto.setItems(items);

            return dto;
        });
    }

    public static void main(String[] args) {
        OrderDetailDAO dao = new OrderDetailDAO();
        System.out.println(dao.findOrderDetailByOrderId(43));
    }

}
