package app;

import controller.*;
import model.BookStore;
import view.Builder;
import view.MenuController;
import view.Navigator;
import view.views.BookView;
import view.views.CartView;
import view.views.OrderView;
import view.views.ReportView;

public class Main {
    public static void main(String[] args) {

        BookStore store = new BookStore();

        BookController bookController = new BookController(store);
        CartController cartController = new CartController(store);
        OrderController orderController = new OrderController(store);
        ReportController reportController = new ReportController(store);

        BookView bookView = new BookView(bookController);
        CartView cartView = new CartView(cartController);
        OrderView orderView = new OrderView(orderController);
        ReportView reportView = new ReportView(reportController);

        Builder builder = Builder.getInstance(bookView, cartView, orderView, reportView);

        Navigator navigator = new Navigator(builder.getRootMenu());
        MenuController menuController = new MenuController(navigator);

        menuController.run();
    }
}
