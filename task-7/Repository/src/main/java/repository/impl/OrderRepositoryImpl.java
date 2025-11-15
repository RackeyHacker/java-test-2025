package repository.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import entity.Book;
import entity.Customer;
import entity.Order;
import enums.OrderStatus;
import repository.BookRepository;
import repository.CustomerRepository;
import repository.OrderRepository;
import repository.exceptions.DataProcessingException;
import repository.util.CsvUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {

    private final String orderDataPath = "data/orders.csv";
    private List<Order> orders = new ArrayList<>();
    private List<Order> requests = new ArrayList<>();

    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;

    public OrderRepositoryImpl(BookRepository bookRepository,
                               CustomerRepository customerRepository) {
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public void removeOrderById(int id) {
        Order order = findOrderById(id);
        orders.remove(order);
    }

    @Override
    public Order findOrderById(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Order> findOrderByCustomerId(int customerId) {
        return orders.stream()
                .filter(order -> order.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findOrderByDateRange(LocalDate startDate, LocalDate endDate) {
        return orders.stream()
                .filter(order -> !order.getExecutionDate().isBefore(startDate) &&
                        !order.getExecutionDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        order.setStatus(newStatus);
    }

    @Override
    public List<Order> findAllRequests() {
        return new ArrayList<>(requests);
    }

    @Override
    public void addRequest(Order order) {
        requests.add(order);
    }

    @Override
    public void removeRequest(Order order) {
        requests.remove(order);
    }

    @Override
    public void replaceAllOrders(List<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
    }

    @Override
    public void exportOrdersCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(orderDataPath))) {

            writer.writeNext(new String[]{"id", "status", "executionDate", "amount", "bookIds", "customerId"});

            for (Order order : orders) {
                StringBuilder bookIds = new StringBuilder();
                for (int i = 0; i < order.getBooks().size(); i++) {
                    int bookId = order.getBooks().get(i).getId();
                    bookIds.append(bookId);
                    if (i < order.getBooks().size() - 1) {
                        bookIds.append(";");
                    }
                }

                writer.writeNext(new String[]{
                        String.valueOf(order.getId()),
                        order.getStatus().toString(),
                        order.getExecutionDate().toString(),
                        order.getAmount().toString(),
                        bookIds.toString(),
                        String.valueOf(order.getCustomer().getId())
                });
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error exporting orders", e);
        }
    }

    @Override
    public void importOrdersCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(orderDataPath))) {

            CsvUtils.skipHeader(reader);
            String[] row;

            while ((row = reader.readNext()) != null) {

                int id = Integer.parseInt(row[0]);
                OrderStatus status = OrderStatus.valueOf(row[1]);
                LocalDate executionDate = LocalDate.parse(row[2]);
                BigDecimal amount = new BigDecimal(row[3]);
                String bookIdsString = row[4];
                int customerId = Integer.parseInt(row[5]);

                List<Book> books = new ArrayList<>();
                if (bookIdsString != null && !bookIdsString.isBlank()) {
                    String[] parts = bookIdsString.split(";");
                    for (String part : parts) {
                        if (part.isEmpty()) continue;
                        try {
                            int bookId = Integer.parseInt(part.trim());
                            Book book = bookRepository.findBookById(bookId);
                            if (book != null) {
                                books.add(book);
                            }
                        } catch (NumberFormatException ex) {
                            System.err.println("Invalid book id in CSV: " + part);
                        }
                    }
                }

                Customer customer = customerRepository.findCustomerById(customerId);

                Order existing = findOrderById(id);

                if (existing == null) {
                    Order order = new Order(books, customer);
                    order.setId(id);
                    order.setStatus(status);
                    order.setExecutionDate(executionDate);
                    order.setAmount(amount);
                    orders.add(order);
                } else {
                    existing.setStatus(status);
                    existing.setExecutionDate(executionDate);
                    existing.setAmount(amount);
                    existing.getBooks().clear();
                    existing.getBooks().addAll(books);
                    existing.setCustomer(customer);
                }
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error importing orders", e);
        }
    }
}
