package DTO;

import model.*;

import java.util.List;
import java.util.Map;

public class OrderDetailDTO {

    private Order order;
    private Shipping shipping;
    private Address address;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
                "order=" + order +
                ", shipping=" + shipping +
                ", address=" + address +
                ", items=" + items +
                '}';
    }
}
