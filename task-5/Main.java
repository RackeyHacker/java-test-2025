import controller.*;
import model.BookStore;
import view.Builder;
import view.Navigator;

import java.math.BigDecimal;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BookStore store = new BookStore();

        BookController bookController = new BookController(store);
        CartController cartController = new CartController(store);
        OrderController orderController = new OrderController(store);
        ReportController reportController = new ReportController(store);

        Builder builder = Builder.getInstance(bookController, cartController, orderController, reportController);
        Navigator navigator = new Navigator(builder.getRootMenu());

        MenuController menuController = new MenuController(navigator);

        menuController.run();
    }
}
