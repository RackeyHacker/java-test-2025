package com.service;

public interface DataService {

    void exportBooks();

    void importBooks();

    void exportOrders();

    void importOrders();

    void exportCustomers();

    void importCustomers();

    void saveToJson();

    void loadFromJson();
}
