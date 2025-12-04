package com.repository.impl;

import com.annotations.Autowired;
import com.annotations.Repository;
import com.config.dbconfig.DBConnectionManager;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.entity.Book;
import com.entity.Customer;
import com.entity.Order;
import com.enums.OrderStatus;
import com.repository.BookRepository;
import com.repository.CustomerRepository;
import com.repository.OrderRepository;
import com.repository.exceptions.DataProcessingException;
import com.repository.util.CsvUtils;

import javax.swing.plaf.nimbus.State;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final String orderDataPath = "data/orders.csv";

    private final DBConnectionManager connectionManager = new DBConnectionManager();

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public OrderRepositoryImpl() {
    }

    public OrderRepositoryImpl(BookRepository bookRepository,
                               CustomerRepository customerRepository) {
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String query = "select * from orders";
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                orders.add(setOrderParameters(resultSet, connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    private Order setOrderParameters(ResultSet resultSet, Connection connection) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("order_id"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setHasPendingRequests(resultSet.getBoolean("has_pending_requests"));
        order.setExecutionDate(LocalDate.parse(resultSet.getString("execution_date")));
        order.setAmount(resultSet.getBigDecimal("amount"));

        int customerId = resultSet.getInt("customer_id");
        Customer customer = customerRepository.findCustomerById(customerId);
        order.setCustomer(customer);

        List<Book> books = new ArrayList<>();
        String queryBooks = "select book_id from order_books where order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryBooks)) {
            preparedStatement.setInt(1, order.getId());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Book book = bookRepository.findBookById(rs.getInt("book_id"));
                    books.add(book);
                }
            }
        }
        order.setBooks(books);
        return order;
    }

    @Override
    public void addOrder(Order order) {
        String queryOrders = "insert into orders (status, has_pending_requests, execution_date, amount, customer_id) " +
                "values (?, ?, ?, ?, ?)";
        String queryOrderBooks = "insert into order_books (order_id, book_id) values (?, ?)";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            int orderId;
            try (PreparedStatement psOrders = connection.prepareStatement(queryOrders, Statement.RETURN_GENERATED_KEYS)) {
                psOrders.setString(1, order.getStatus().name());
                psOrders.setBoolean(2, order.isHasPendingRequests());
                psOrders.setDate(3, Date.valueOf(order.getExecutionDate()));
                psOrders.setBigDecimal(4, order.getAmount());
                psOrders.setInt(5, order.getCustomer().getId());
                psOrders.executeUpdate();

                try (ResultSet rs = psOrders.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                        order.setId(orderId);
                    } else {
                        throw new SQLException("Failed to obtain generated order ID");
                    }
                }

            }
            try (PreparedStatement psOrderBooks = connection.prepareStatement(queryOrderBooks)) {
                List<Book> books = order.getBooks();
                for (Book book : books) {
                    psOrderBooks.setInt(1, order.getId());
                    psOrderBooks.setInt(2, book.getId());
                    psOrderBooks.addBatch();
                }
                psOrderBooks.executeBatch();
            }
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void removeOrderById(int id) {
        String query = "delete from orders where order_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order findOrderById(int id) {
        String query = "select * from orders where order_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return setOrderParameters(resultSet, connection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> findOrderByCustomerId(int customerId) {
        String query = "select * from orders where customer_id = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(setOrderParameters(resultSet, connection));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findOrderByDateRange(LocalDate startDate, LocalDate endDate) {
        String query = "select * from orders where execution_date between ? and ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(setOrderParameters(resultSet, connection));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void changeOrderStatus(int orderId, OrderStatus newStatus) {
        String query = "update orders set status = ? where order_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, newStatus.name());
            preparedStatement.setInt(2, orderId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> findAllRequests() {
        String query = "select * from orders where has_pending_requests = true";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                orders.add(setOrderParameters(resultSet, connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void addRequest(Order order) {
        String query = "update orders set has_pending_requests = true where id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRequest(Order order) {
        String query = "update orders set has_pending_requests = false where id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceAllOrders(List<Order> orders) {
        String truncateOrderBooks = "truncate table order_books restart identity cascade";
        String truncateOrders = "truncate table orders restart identity cascade";
        String insertOrder =
                "insert into orders (status, has_pending_requests, execution_date, amount, customer_id) " +
                        "values (?, ?, ?, ?, ?)";
        String insertOrderBooks =
                "insert into order_books (order_id, book_id) values (?, ?)";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement st = connection.createStatement()) {
                st.executeUpdate(truncateOrderBooks);
                st.executeUpdate(truncateOrders);
            }
            try (PreparedStatement psOrders =
                         connection.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psBooks =
                         connection.prepareStatement(insertOrderBooks)
            ) {
                for (Order order : orders) {
                    psOrders.setString(1, order.getStatus().name());
                    psOrders.setBoolean(2, order.isHasPendingRequests());
                    psOrders.setDate(3, Date.valueOf(order.getExecutionDate()));
                    psOrders.setBigDecimal(4, order.getAmount());
                    psOrders.setInt(5, order.getCustomer().getId());
                    psOrders.executeUpdate();
                    int generatedOrderId;
                    try (ResultSet rs = psOrders.getGeneratedKeys()) {
                        rs.next();
                        generatedOrderId = rs.getInt(1);
                    }
                    for (Book book : order.getBooks()) {
                        psBooks.setInt(1, generatedOrderId);
                        psBooks.setInt(2, book.getId());
                        psBooks.addBatch();
                    }
                }

                psBooks.executeBatch();
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportOrdersCSV() {
        List<Order> orders = findAll();
        try (CSVWriter writer = new CSVWriter(new FileWriter(orderDataPath))) {
            writer.writeNext(new String[]{"order_id", "status", "executionDate", "amount", "bookIds", "customerId"});
            for (Order order : orders) {
                String bookIds = order.getBooks()
                        .stream()
                        .map(book -> String.valueOf(book.getId()))
                        .collect(Collectors.joining(";"));
                writer.writeNext(new String[]{
                        String.valueOf(order.getId()),
                        order.getStatus().name(),
                        order.getExecutionDate().toString(),
                        order.getAmount().toString(),
                        bookIds,
                        String.valueOf(order.getCustomer().getId())
                });
            }
        } catch (Exception e) {
            throw new DataProcessingException("Error exporting orders", e);
        }
    }

    @Override
    public void importOrdersCSV() {
        List<Order> imported = new ArrayList<>();
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
                    for (String part : bookIdsString.split(";")) {
                        try {
                            int bookId = Integer.parseInt(part.trim());
                            Book book = bookRepository.findBookById(bookId);
                            if (book != null) books.add(book);
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
                Customer customer = customerRepository.findCustomerById(customerId);
                Order order = new Order(books, customer);
                order.setId(id);
                order.setStatus(status);
                order.setExecutionDate(executionDate);
                order.setAmount(amount);
                imported.add(order);
            }
        } catch (Exception e) {
            throw new DataProcessingException("Error importing orders", e);
        }
        replaceAllOrders(imported);
    }
}
