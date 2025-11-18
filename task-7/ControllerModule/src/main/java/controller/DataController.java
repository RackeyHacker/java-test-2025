package controller;

import service.DataService;

public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    public void exportBooks() {
        dataService.exportBooks();
    }

    public void importBooks() {
        dataService.importBooks();
    }

    public void exportOrders() {
        dataService.exportOrders();
    }

    public void importOrders() {
        dataService.importOrders();
    }

    public void exportCustomers() {
        dataService.exportOrders();
    }

    public void importCustomers() {
        dataService.importOrders();
    }
}
