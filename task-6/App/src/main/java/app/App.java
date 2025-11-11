package app;

import controller.*;
import model.models.BookStore;
import view.Builder;
import view.MenuController;
import view.Navigator;
import view.views.*;

public class App {
    public static void main(String[] args) {

        BookStore store = new BookStore();

        BookController bookController = new BookController(store);
        CartController cartController = new CartController(store);
        OrderController orderController = new OrderController(store);
        ReportController reportController = new ReportController(store);
        DataController dataController = new DataController(store);

        BookView bookView = new BookView(bookController);
        CartView cartView = new CartView(cartController);
        OrderView orderView = new OrderView(orderController);
        ReportView reportView = new ReportView(reportController);
        DataView dataView = new DataView(dataController);

        Builder builder = Builder.getInstance(bookView, cartView, orderView, reportView, dataView);

        Navigator navigator = new Navigator(builder.getRootMenu());
        MenuController menuController = new MenuController(navigator);

        menuController.run();
    }
}
