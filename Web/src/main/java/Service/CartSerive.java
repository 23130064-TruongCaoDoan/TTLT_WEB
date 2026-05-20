package Service;

import dao.CartDao;
import model.Cart;
import model.CartItem;

import java.util.List;

public class CartSerive {
    private CartDao cartDao;

    public CartSerive() {
        this.cartDao= new CartDao();
    }

    public void addToCart(int id, int bookId, int quantity) {
        Cart cart = cartDao.getCart(id);
        cartDao.addToCart(cart.getId(),bookId,quantity);
    }
    public Cart getCart(int userId) {
        Cart cart = cartDao.getCart(userId);
        if (cart == null) {
            cartDao.createCart(userId);
            cart = cartDao.getCart(userId);
        }

        return cart;
    }

    public void updateItem(int cart_id, int id1, int quantity) {
        cartDao.updateItem(cart_id,  id1,  quantity);
    }

    public void removeItem(int cart_id, int id1) {
        cartDao.removeItem(cart_id,id1);
    }

    public void removeAllItems(int cart_id) {
        cartDao.removeAllItems(cart_id);
    }
}
