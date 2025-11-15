package service.impl;

import entity.Book;
import repository.BookRepository;
import service.CartService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class CartServiceImpl implements CartService {

    private final BookRepository bookRepository;
    private final List<Book> cart = new ArrayList<>();

    public CartServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
