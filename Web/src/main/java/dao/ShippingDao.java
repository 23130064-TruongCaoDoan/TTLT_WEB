package dao;

public class ShippingDao extends BaseDao {

    public int addShipping(int orderId, int addressId, String shippingType, double shippingCost, String deliveredDate) {
        try {
            return getJdbi().withHandle(handle ->
                    handle.createUpdate("""
                                        INSERT INTO SHIPPING
                                        (order_id, address_id, shipping_type, shipping_cost, delivered_date, status)
                                        VALUES (:order_id, :address_id, :shipping_type, :shipping_cost, :delivered_date, :status)
                                    """)
                            .bind("order_id", orderId)
                            .bind("address_id", addressId)
                            .bind("shipping_type", shippingType)
                            .bind("shipping_cost", shippingCost)
                            .bind("delivered_date", deliveredDate)
                            .bind("status", "PENDING")
                            .executeAndReturnGeneratedKeys("shipping_id")
                            .mapTo(int.class)
                            .one()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
