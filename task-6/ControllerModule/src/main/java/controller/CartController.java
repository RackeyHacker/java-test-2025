package controller;

import entity.Book;
import model.models.BookStore;

import java.util.List;

public class CartController  {

    private final BookStore store;

    public CartController(BookStore store) {
        this.store = store;
    }

    public boolean addBookToCart(String title) {
        return store.addBookToCart(title);
    }

    public boolean removeBookFromCart(String title) {
        return store.removeBookFromCart(title);
    }

    public void clearCart() {
        store.clearCart();
    }

    public List<Book> getCart() {
        return store.getCart();
    }
}