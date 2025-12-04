package com.service.impl;

import com.annotations.Autowired;
import com.annotations.Service;
import com.entity.Book;
import com.repository.BookRepository;
import com.service.CartService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private BookRepository bookRepository;
    private final List<Book> cart = new ArrayList<>();

    public CartServiceImpl() {
    }

    @Override
    public boolean addBookToCart(String title) {
        Book book = bookRepository.findBookByTitle(title);
        if (book == null) throw new NoSuchElementException("The book not found: " + title);
        return cart.add(book);
    }

    @Override
    public boolean removeBookFromCart(String title) {
        Book book = bookRepository.findBookByTitle(title);
        if (book == null) throw new NoSuchElementException("The book not found: " + title);
        return cart.remove(book);
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

    @Override
    public List<Book> getCart() {
        return new ArrayList<>(cart);
    }
}
