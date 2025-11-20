package com.view;

import com.view.views.*;

public class Builder {

    private static Builder instance;

    private Menu rootMenu;
    private final BookView bookView;
    private final CartView cartView;
    private final OrderView orderView;
    private final ReportView reportView;
    private final DataView dataView;

    private Builder(BookView bookView,
                    CartView cartView,
                    OrderView orderView,
                    ReportView reportView,
                    DataView dataView) {
        this.bookView = bookView;
        this.cartView = cartView;
        this.orderView = orderView;
        this.reportView = reportView;
        this.dataView = dataView;

        buildMenu();
    }

    public static Builder getInstance(BookView bookView,
                                      CartView cartView,
                                      OrderView orderView,
                                      ReportView reportView,
                                      DataView dataView) {
        if (instance == null) {
            instance = new Builder(bookView, cartView, orderView, reportView, dataView);
        }
        return instance;
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu() {

        Menu bookMenu = new Menu("Book Management", new MenuItem[]{
                new MenuItem("Add book to warehouse", bookView::addBookToWarehouse, null),
                new MenuItem("Write off book from warehouse", bookView::writeOffBookFromWarehouse, null),
                new MenuItem("Change book status", bookView::changeBookStatus, null),
                new MenuItem("Show books sorted by", bookView::showBooksSortedBy, null),
                new MenuItem("Show book description", bookView::showBookDescription, null),
                new MenuItem("Show requested books sorted by", bookView::showRequestedBooksSortedBy, null),
                new MenuItem("Show old books", bookView::showOldBooks, null),
                new MenuItem("Back", null, null)
        });

        Menu cartMenu = new Menu("Cart Management", new MenuItem[]{
                new MenuItem("Add book to cart", cartView::addBookToCart, null),
                new MenuItem("Remove book from cart", cartView::removeBookFromCart, null),
                new MenuItem("Clear cart", cartView::clearCart, null),
                new MenuItem("Show cart", cartView::showCart, null),
                new MenuItem("Back", null, null)
        });

        Menu orderMenu = new Menu("Order Management", new MenuItem[]{
                new MenuItem("Make order", orderView::makeOrder, null),
                new MenuItem("Cancel order", orderView::cancelOrder, null),
                new MenuItem("Change order status", orderView::changeOrderStatus, null),
                new MenuItem("Show orders sorted by", orderView::showOrdersSortedBy, null),
                new MenuItem("Show order details", orderView::showOrderDetails, null),
                new MenuItem("Back", null, null)
        });

        Menu reportMenu = new Menu("Reports", new MenuItem[]{
                new MenuItem("Show orders for period sorted by", reportView::showOrderForDateSortedBy, null),
                new MenuItem("Find earned funds over period", reportView::findEarnedFundsOverPeriodTime, null),
                new MenuItem("Find number of completed orders", reportView::findNumberCompletedOrdersOverPeriodTime, null),
                new MenuItem("Back", null, null)
        });

        Menu dataMenu = new Menu("DataManagement", new MenuItem[] {
                new MenuItem("Export books", dataView::exportBooks, null),
                new MenuItem("Import books", dataView::importBooks, null),
                new MenuItem("Export customers", dataView::exportCustomers, null),
                new MenuItem("Import customers", dataView::importCustomers, null),
                new MenuItem("Export orders", dataView::exportOrders, null),
                new MenuItem("Import orders", dataView::importOrders, null),
                new MenuItem("Back", null, null)
        });

        Menu mainMenu = new Menu("Main Menu", new MenuItem[]{
                new MenuItem("Book management", null, bookMenu),
                new MenuItem("Cart management", null, cartMenu),
                new MenuItem("Order management", null, orderMenu),
                new MenuItem("Reports", null, reportMenu),
                new MenuItem("Data management", null, dataMenu),
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
        for (MenuItem item : dataMenu.getMenuItems()) {
            if ("Back".equals(item.getTitle())) item.setNextMenu(mainMenu);
        }

        this.rootMenu = mainMenu;
    }
}
