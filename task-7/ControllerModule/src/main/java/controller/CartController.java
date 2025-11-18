package controller;

import entity.Book;
import service.CartService;

import java.util.List;

public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    public boolean addBookToCart(String title) {
        return cartService.addBookToCart(title);
    }

    public boolean removeBookFromCart(String title) {
        return cartService.removeBookFromCart(title);
    }

    public void clearCart() {
        cartService.clearCart();
    }

    public List<Book> getCart() {
        return cartService.getCart();
    }
}