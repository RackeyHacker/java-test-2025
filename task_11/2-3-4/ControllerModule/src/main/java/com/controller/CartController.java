package com.controller;

import com.annotations.Autowired;
import com.annotations.Controller;
import com.entity.Book;
import com.service.CartService;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    public CartController() {
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