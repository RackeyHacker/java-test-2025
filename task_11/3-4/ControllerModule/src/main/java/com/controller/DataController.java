package com.controller;

import com.annotations.Autowired;
import com.annotations.Controller;
import com.service.DataService;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    public DataController() {
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
