package view;

import controller.BookController;
import controller.CartController;
import controller.OrderController;
import controller.ReportController;

public class Builder {

    private static Builder instance;

    private Menu rootMenu;
    private final BookController bookController;
    private final CartController cartController;
    private final OrderController orderController;
    private final ReportController reportController;

    private Builder(BookController bookController,
                    CartController cartController,
                    OrderController orderController,
                    ReportController reportController) {
        this.bookController = bookController;
        this.cartController = cartController;
        this.orderController = orderController;
        this.reportController = reportController;

        buildMenu();
    }

    public static Builder getInstance(BookController bookController,
                                      CartController cartController,
                                      OrderController orderController,
                                      ReportController reportController) {
        if (instance == null) {
            instance = new Builder(bookController, cartController, orderController, reportController);
        }
        return instance;
    }

    public Menu getRootMenu() {
        return rootMenu;
    }


    public void buildMenu() {

        Menu bookMenu = new Menu("Book Management", new MenuItem[]{
                new MenuItem("Add book to warehouse", bookController::addBookToWarehouse, null),
                new MenuItem("Write off book from warehouse", bookController::writeOffBookFromWarehouse, null),
                new MenuItem("Change book status", bookController::changeBookStatus, null),
                new MenuItem("Show books sorted by", bookController::showBooksSortedBy, null),
                new MenuItem("Show book description", bookController::showBookDescription, null),
                new MenuItem("Show requested books sorted by", bookController::showRequestedBooksSortedBy, null),
                new MenuItem("Show old books", bookController::showOldBooks, null),
                new MenuItem("Back", null, null)
        });

        Menu cartMenu = new Menu("Cart Management", new MenuItem[]{
                new MenuItem("Add book to cart", cartController::addBookToCart, null),
                new MenuItem("Remove book from cart", cartController::removeBookFromCart, null),
                new MenuItem("Clear cart", cartController::clearCart, null),
                new MenuItem("Show cart", cartController::showCart, null),
                new MenuItem("Back", null, null)
        });

        Menu orderMenu = new Menu("Order Management", new MenuItem[]{
                new MenuItem("Make order", orderController::makeOrder, null),
                new MenuItem("Cancel order", orderController::cancelOrder, null),
                new MenuItem("Change order status", orderController::changeOrderStatus, null),
                new MenuItem("Show orders sorted by", orderController::showOrdersSortedBy, null),
                new MenuItem("Show order details", orderController::showOrderDetails, null),
                new MenuItem("Back", null, null)
        });

        Menu reportMenu = new Menu("Reports", new MenuItem[]{
                new MenuItem("Show orders for period sorted by", reportController::showOrderForDateSortedBy, null),
                new MenuItem("Find earned funds over period", reportController::findEarnedFundsOverPeriodTime, null),
                new MenuItem("Find number of completed orders", reportController::findNumberCompletedOrdersOverPeriodTime, null),
                new MenuItem("Back", null, null)
        });

        Menu mainMenu = new Menu("Main Menu", new MenuItem[]{
                new MenuItem("Book management", null, bookMenu),
                new MenuItem("Cart management", null, cartMenu),
                new MenuItem("Order management", null, orderMenu),
                new MenuItem("Reports", null, reportMenu),
                new MenuItem("Exit", () -> System.exit(0), null)
        });

        for (MenuItem item : bookMenu.getMenuItems()) {
            if ("Back".equals(item.getTitle())) item.setNextMenu(mainMenu);
        }
        for (MenuItem item : cartMenu.getMenuItems()) {
            if ("Back".equals(item.getTitle())) item.setNextMenu(mainMenu);
        }
        for (MenuItem item : orderMenu.getMenuItems()) {
            if ("Back".equals(item.getTitle())) item.setNextMenu(mainMenu);
        }
        for (MenuItem item : reportMenu.getMenuItems()) {
            if ("Back".equals(item.getTitle())) item.setNextMenu(mainMenu);
        }

        this.rootMenu = mainMenu;
    }
}
