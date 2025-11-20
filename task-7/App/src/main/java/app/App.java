package app;

import controller.*;
import repository.BookRepository;
import repository.CustomerRepository;
import repository.OrderRepository;
import repository.impl.BookRepositoryImpl;
import repository.impl.CustomerRepositoryImpl;
import repository.impl.OrderRepositoryImpl;
import service.*;
import service.impl.*;
import view.Builder;
import view.MenuController;
import view.Navigator;
import view.views.*;

public class App {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepositoryImpl();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        OrderRepository orderRepository = new OrderRepositoryImpl(bookRepository, customerRepository);

        CartService cartService = new CartServiceImpl(bookRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, customerRepository, null, cartService);
        BookService bookService = new BookServiceImpl(bookRepository, orderService);
        ReportService reportService = new ReportServiceImpl(orderRepository);
        DataService dataService = new DataServiceImpl(bookRepository, orderRepository, customerRepository);

        dataService.loadFromJson();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            dataService.saveToJson();
            System.out.println("Saving data to files...");
        }));

        BookController bookController = new BookController(bookService);
        CartController cartController = new CartController(cartService);
        OrderController orderController = new OrderController(orderService, cartService);
        ReportController reportController = new ReportController(reportService);
        DataController dataController = new DataController(dataService);

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
