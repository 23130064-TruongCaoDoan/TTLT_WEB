package dao;

import model.Address;

public class ShippingDao extends BaseDao {

    public int addShipping(int orderId, Address address, String shippingType, double shippingCost, String deliveredDate) {
        try {
            return getJdbi().withHandle(handle ->
                    handle.createUpdate("""
                                        INSERT INTO SHIPPING
                                        (order_id, receiver, phone, ward, districts, city, specificAddress, shipping_type, shipping_cost, delivered_date, status)
                                        VALUES (:order_id, :receiver, :phone, :ward, :districts, :city, :specificAddress, :shipping_type, :shipping_cost, :delivered_date, :status)
                                    """)
                            .bind("order_id", orderId)
                            .bind("receiver", address.getName())
                            .bind("phone", address.getPhone())
                            .bind("ward", address.getWard())
                            .bind("districts", address.getDistricts())
                            .bind("city", address.getCity())
                            .bind("specificAddress", address.getSpecificAddress())
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
