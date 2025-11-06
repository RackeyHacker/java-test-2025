package view.views;

import entity.Book;
import controller.CartController;

import java.util.List;
import java.util.Scanner;

public class CartView {

    private final CartController controller;
    private final Scanner scanner = new Scanner(System.in);

    public CartView(CartController controller) {
        this.controller = controller;
    }

    public void addBookToCart() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        boolean success = controller.addBookToCart(title);
        if (success) {
            System.out.println("The book " + title + " was added to the cart");
        } else {
            System.out.println("The book " + title + " was not found in the store");
        }
    }

    public void removeBookFromCart() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        boolean success = controller.removeBookFromCart(title);
        if (success) {
            System.out.println("The book " + title + " was removed from the cart");
        } else {
            System.out.println("The book " + title + " is not in the cart");
        }
    }

    public void clearCart() {
        controller.clearCart();
        System.out.println("The cart is now empty");
    }

    public void showCart() {
        List<Book> cart = controller.getCart();
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty");
        } else {
            System.out.println("\nYour cart:");
            printCart(cart);
        }
    }

    private void printCart(List<Book> cart) {
        for (int i = 0; i < cart.size(); i++) {
            System.out.println((i + 1) + ". " + cart.get(i));
        }
    }
}