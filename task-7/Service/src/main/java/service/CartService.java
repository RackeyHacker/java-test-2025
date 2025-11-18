package service;

import entity.Book;

import java.util.List;

public interface CartService {

    boolean addBookToCart(String title);

    boolean removeBookFromCart(String title);

    void clearCart();

    List<Book> getCart();
}
