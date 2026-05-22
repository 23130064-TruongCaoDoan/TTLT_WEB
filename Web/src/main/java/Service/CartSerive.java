package Service;

import dao.BookDao;
import dao.CartDao;
import model.Book;
import model.Cart;
import model.CartItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartSerive {
    private CartDao cartDao;
    private BookDao bookDao;

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

    public boolean checkCart(cart.Cart cartGuest) {
        if (cartGuest == null || cartGuest.getItems().isEmpty()) return false;

        bookDao = new BookDao();
        boolean allValid = true;

        List<cart.CartItem> items = new ArrayList<>(cartGuest.getItems());
        for (cart.CartItem item : items) {
            boolean valid = true;

            if (item.getBook() == null) {
                valid = false;
            } else {
                model.Book book = bookDao.getBookById(item.getBook().getId());
                if (book == null || !book.getIsSell() || item.getQuantity() <= 0 || item.getQuantity() > book.getStock()) {
                    valid = false;
                }
            }

            if (!valid) {
                cartGuest.removeItem(item.getBook().getId());
                allValid = false;
            }
        }
        return allValid;
    }

    public boolean checkCart(Cart cartDB) {
        if (cartDB == null || cartDB.getItems().isEmpty()) return false;

        bookDao = new BookDao();
        boolean allValid = true;

        List<CartItem> items = new ArrayList<>(cartDB.getItems());
        for (CartItem item : items) {
            boolean valid = true;

            if (item.getBook() == null) {
                valid = false;
            } else {
                model.Book book = bookDao.getBookById(item.getBook().getId());
                if (book == null || !book.getIsSell() || item.getQuantity() <= 0 || item.getQuantity() > book.getStock()) {
                    valid = false;
                }
            }

            if (!valid) {
                cartDao.removeItem(cartDB.getId(), item.getBook().getId());
                allValid = false;
            }
        }
        return allValid;
    }

    public void mergerCart(cart.Cart cart, int id) {
        Cart c= cartDao.getCart(id);
        if (c == null) {
            cartDao.createCart(id);
            c = cartDao.getCart(id);
        }
        List<CartItem> items1 = new ArrayList<>(c.getItems());

        List<cart.CartItem> items = new ArrayList<>(cart.getItems());
        for (cart.CartItem item : items) {
            boolean valid = true;
            for (CartItem item1 : items1) {
                if (item.getBook().getId() == item1.getBook().getId()) {
                    valid = false;
                    bookDao=new BookDao();
                    Book book = bookDao.getBookById(item1.getBook().getId());
                    if(item.getQuantity() + item1.getQuantity() >book.getStock()){
                        cartDao.updateItem(c.getId(), item1.getBook().getId(), book.getStock());
                        break;
                    }
                    else{
                        cartDao.addToCart(c.getId(), item1.getBook().getId(), item.getQuantity());
                        break;
                    }

                }
            }
            if (valid) {
                cartDao.addToCart(c.getId(), item.getBook().getId(), item.getQuantity());
            }
        }
    }
}
