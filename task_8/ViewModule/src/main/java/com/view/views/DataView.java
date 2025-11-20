package com.view.views;

import com.controller.DataController;

public class DataView {

    private final DataController controller;

    public DataView(DataController controller) {
        this.controller = controller;
    }

    public void importBooks() {

        try {
            controller.importBooks();
            System.out.println("Books imported successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void exportBooks() {
        try {
            controller.exportBooks();
            System.out.println("Book exported successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void importOrders() {
        try {
            controller.importOrders();
            System.out.println("Orders imported successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void exportOrders() {
        try {
            controller.exportOrders();
            System.out.println("Orders exported successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void importCustomers() {
        try {
            controller.importCustomers();
            System.out.println("Customers imported successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void exportCustomers() {
        try {
            controller.exportCustomers();
            System.out.println("Customers exported successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

}
