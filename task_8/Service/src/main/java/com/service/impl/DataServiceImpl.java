package com.service.impl;

import com.annotations.Autowired;
import com.annotations.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.entity.Book;
import com.entity.Customer;
import com.entity.Order;
import com.repository.BookRepository;
import com.repository.CustomerRepository;
import com.repository.OrderRepository;
import com.service.DataService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    private final String jsonPath = "data/json/";
    private final String bookJsonPath = "books.json";
    private final String orderJsonPath = "orders.json";
    private final String customerJsonPath = "customers.json";

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public DataServiceImpl() {
    }

    @Override
    public void exportBooks() {
        bookRepository.exportBooksCSV();
    }

    @Override
    public void importBooks() {
        bookRepository.importBooksCSV();
    }

    @Override
    public void exportOrders() {
        orderRepository.exportOrdersCSV();
    }

    @Override
    public void importOrders() {
        orderRepository.importOrdersCSV();
    }

    @Override
    public void exportCustomers() {
        customerRepository.exportCustomersCSV();
    }

    @Override
    public void importCustomers() {
        customerRepository.importCustomersCSV();
    }

    @Override
    public void saveToJson() {
        try {
            mapper.writeValue(Paths.get(jsonPath, bookJsonPath).toFile(), bookRepository.findAll());
            mapper.writeValue(Paths.get(jsonPath, orderJsonPath).toFile(), orderRepository.findAll());
            mapper.writeValue(Paths.get(jsonPath, customerJsonPath).toFile(), customerRepository.findAll());
        } catch (Exception e) {
            System.err.println("Error while saving to json: " + e.getMessage());
        }
    }

    @Override
    public void loadFromJson() {
        try {

            checkJsonFile(bookJsonPath);
            checkJsonFile(orderJsonPath);
            checkJsonFile(customerJsonPath);

            List<Book> loadedBooks = readDataFromFile(bookJsonPath, Book[].class);
            bookRepository.replaceAllBooks(loadedBooks);

            List<Order> loadedOrders = readDataFromFile(orderJsonPath,  Order[].class);
            orderRepository.replaceAllOrders(loadedOrders);

            List<Customer> loadedCustomers = readDataFromFile(customerJsonPath, Customer[].class);
            customerRepository.replaceAllCustomers(loadedCustomers);

            System.out.println("All JSON-data are loaded successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkJsonFile(String fileName) throws IOException {

        File file = Paths.get(jsonPath, fileName).toFile();

        if (!file.exists() || file.length() == 0) {
            Files.writeString(file.toPath(), "[]");
        }
    }

    private <T> List<T> readDataFromFile(String fileName, Class<T[]> clazz) throws IOException {
        T[] array = mapper.readValue(Paths.get(jsonPath, fileName).toFile(), clazz);
        return Arrays.asList(array);
    }
}
