package controller;

import entity.Book;
import model.BookStore;

import java.util.Scanner;

public class CartController {

    private BookStore store;
    private final Scanner scanner = new Scanner(System.in);

    public CartController(BookStore store) {
        this.store = store;
    }

    public void addBookToCart() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        Book book = store.findBookByTitle(title);
        store.addBookToCart(book);
    }

    public void removeBookFromCart() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        Book book = store.findBookByTitle(title);
        store.removeBookFromCart(book);
    }

    public void clearCart() {
        store.clearCart();
    }

    public void showCart() {
        store.showCart();
    }
}
