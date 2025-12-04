package app;

import com.DI;
import com.config.dbconfig.DBConnectionManager;
import com.controller.*;
import com.service.DataService;
import com.view.Builder;
import com.view.MenuController;
import com.view.Navigator;
import com.view.views.*;

import java.sql.Connection;

public class App {

    public static void main(String[] args) {

        DBConnectionManager manager = new DBConnectionManager();
        try (Connection connection = manager.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful");
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }

        DI di = DI.getInstance();

        BookController bookController = di.get(BookController.class);
        CartController cartController = di.get(CartController.class);
        OrderController orderController = di.get(OrderController.class);
        ReportController reportController = di.get(ReportController.class);
        DataController dataController = di.get(DataController.class);

        DataService dataService = di.get(DataService.class);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            dataService.saveToJson();
            System.out.println("Saving data to JSON...");
        }));

        BookView bookView = new BookView(bookController);
        CartView cartView = new CartView(cartController);
        OrderView orderView = new OrderView(orderController);
        ReportView reportView = new ReportView(reportController);
        DataView dataView = new DataView(dataController);

        Builder builder = Builder.getInstance(
                bookView, cartView, orderView, reportView, dataView
        );

        Navigator navigator = new Navigator(builder.getRootMenu());
        MenuController menuController = new MenuController(navigator);

        menuController.run();
    }
}
