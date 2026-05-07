package DTO;

import model.*;

import java.util.List;
import java.util.Map;

public class OrderDetailDTO {

    private Order order;
    private Shipping shipping;
    private List<OrderItemDTO> items;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }


    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "order=" + order.toString() +
                ", shipping=" + shipping.toString() +
                ", items=" + items +
                '}';
    }
}
