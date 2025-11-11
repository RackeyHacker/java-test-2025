package controller;

import model.models.BookStore;

public class DataController {

    private final BookStore store;

    public DataController(BookStore store) {
        this.store = store;
    }

    public void exportBooks() {
        store.exportBookCSV();
    }

    public void importBooks() {
        store.importBookCSV();
    }

    public void exportOrders() {
        store.exportOrdersCSV();
    }

    public void importOrders() {
        store.importOrdersCSV();
    }

    public void exportCustomers() {
        store.exportCustomersCSV();
    }

    public void importCustomers() {
        store.importCustomersCSV();
    }
}
