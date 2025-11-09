package model;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import entity.Book;
import entity.Customer;
import entity.Order;
import enums.BookStatus;
import enums.OrderStatus;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class BookStore {

    private final String bookData = "data/books.csv";
    private final String orderData = "data/orders.csv";
    private final String customerData = "data/customers.csv";

    private final List<Book> warehouse = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Book> cart = new ArrayList<>();
    private final List<Order> requests = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    public Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    public List<Book> getCart() {
        return cart;
    }

    public Order makeOrder(List<Book> cart, Customer customer) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("The cart is null or empty");
        }

        if (findCustomerById(customer.getId()) == null) {
            customers.add(customer);
        }

        Order order = new Order(new ArrayList<>(cart), customer);

        List<Book> unavailableBooks = findUnavailableBooks(cart);

        if (!unavailableBooks.isEmpty()) {
            for (Book book : unavailableBooks) {
                makeRequestForBook(book);
            }
            return handleOrderWithUnavailableBooks(order);
        }
        return handleSuccessfulOrder(order);
    }

    private List<Book> findUnavailableBooks(List<Book> cart) {
        List<Book> unavailableBooks = new ArrayList<>();
        for (Book book : cart) {
            if (book == null) throw new IllegalArgumentException("The book is null");
            if (!checkStatus(book)) {
                unavailableBooks.add(book);
            }
        }
        return unavailableBooks;
    }

    private Order handleOrderWithUnavailableBooks(Order order) {
        order.setHasPendingRequests(true);
        requests.add(order);
        clearCart();
        return order;
    }

    private Order handleSuccessfulOrder(Order order) {
        orders.add(order);
        clearCart();
        return order;
    }

    public void cancelOrder(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid order ID");

        Order order = findOrderById(id);
        if (order == null) throw new IllegalArgumentException("Order #" + id + " not found");

        order.setStatus(OrderStatus.CANCELED);
    }

    public void changeOrderStatus(int id, OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status is null");
        }

        Order order = findOrderById(id);
        if (order != null) {
            order.setStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Order #" + id + " not found");
        }
    }

    public List<Order> changeBookStatus(String title, BookStatus newStatus) {
        Book searchingBook = findBookByTitle(title);
        if (searchingBook == null) {
            throw new NoSuchElementException("Book not found: " + title);
        }

        searchingBook.setStatus(newStatus);

        if (newStatus == BookStatus.AVAILABLE) {
            return processBookRequests(searchingBook);
        }
        return List.of();
    }

    public void addBookToWarehouse(Book book) {
        if (book == null) throw new IllegalArgumentException("The book is null");
        warehouse.add(book);
        book.setStatus(BookStatus.AVAILABLE);
        processBookRequests(book);
    }

    public void writeOffBookFromWarehouse(String title) {
        Book book = findBookByTitle(title);
        if (book == null) throw new IllegalArgumentException("The book is null");
        book.setStatus(BookStatus.UNAVAILABLE);
    }

    public void makeRequestForBook(Book book) {
        book.incrementRequestCount();
    }

    public boolean addBookToCart(String title) {
        Book book = findBookByTitle(title);
        if (book == null) return false;
        return cart.add(book);
    }

    public boolean removeBookFromCart(String title) {
        Book book = findBookByTitle(title);
        if (book == null) return false;
        return cart.remove(book);
    }

    public void clearCart() {
        cart.clear();
    }

    public Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) return order;
        }
        for (Order order : requests) {
            if (order.getId() == id) return order;
        }
        throw new NoSuchElementException("Order #" + id + " not found");
    }

    public Book findBookByTitle(String title) {
        if (title == null) return null;
        return warehouse.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public Book findBookById(int id) {
        for (Book b : warehouse) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    private boolean checkStatus(Book book) {
        return book.getStatus() != BookStatus.UNAVAILABLE;
    }

    private List<Order> processBookRequests(Book availableBook) {
        List<Order> readyOrders = new ArrayList<>();
        for (Order order : requests) {
            boolean containsThisBook = false;
            for (Book b : order.getBooks()) {
                if (b.getTitle().equals(availableBook.getTitle())) {
                    containsThisBook = true;
                    break;
                }
            }
            if (!containsThisBook) {
                continue;
            }
            boolean allAvailable = true;
            for (Book b : order.getBooks()) {
                if (b.getStatus() == BookStatus.UNAVAILABLE) {
                    allAvailable = false;
                    break;
                }
            }
            if (allAvailable) {
                order.setHasPendingRequests(false);
                order.setStatus(OrderStatus.NEW);
                readyOrders.add(order);
            }
        }
        requests.removeAll(readyOrders);
        orders.addAll(readyOrders);
        return readyOrders;
    }


    public List<Book> showBooksSortedBy(String sortedBy) {
        if (warehouse.isEmpty()) return Collections.emptyList();

        return sortBooks(sortedBy, warehouse);
    }

    private List<Book> sortBooks(String sortedBy, List<Book> bookList) {
        switch (sortedBy) {
            case "title":
                bookList.sort(Comparator.comparing(Book::getTitle));
                break;
            case "publicationDate":
                bookList.sort(Comparator.comparing(Book::getPublicationDate));
                break;
            case "price":
                bookList.sort(Comparator.comparing(Book::getPrice));
                break;
            case "status":
                bookList.sort(Comparator.comparing(Book::getStatus));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortedBy);
        }
        return bookList;
    }


    public List<Order> showOrdersSortedBy(String sortedBy) {
        if (orders.isEmpty()) return Collections.emptyList();

        return sortOrders(sortedBy, orders);
    }

    private List<Order> sortOrders(String sortedBy, List<Order> orderList) {
        switch (sortedBy) {
            case "executionDate":
                orderList.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                orderList.sort(Comparator.comparing(Order::getAmount));
                break;
            case "status":
                orderList.sort(Comparator.comparing(Order::getStatus));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort parameter: " + sortedBy);
        }

        return orderList;
    }


    private List<Order> findOrdersBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = orders.stream()
                .filter(order -> !order.getExecutionDate().isBefore(startDate))
                .filter(order -> !order.getExecutionDate().isAfter(endDate))
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .toList();

        if (sortedOrders.isEmpty()) {
            return Collections.emptyList();
        } else {
            return sortedOrders;
        }
    }

    public List<Order> showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortedBy) {

        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (sortedOrders.isEmpty()) return Collections.emptyList();

        switch (sortedBy) {
            case "date":
                sortedOrders.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                sortedOrders.sort(Comparator.comparing(Order::getAmount));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortedBy);
        }

        return sortedOrders;
    }

    private boolean checkOrdersListEmpty(List<Order> list) {
        return list.isEmpty();
    }

    public BigDecimal findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (checkOrdersListEmpty(sortedOrders)) return new BigDecimal("0");

        BigDecimal totalAmount = new BigDecimal("0");
        for (Order order : sortedOrders) {
            totalAmount = totalAmount.add(order.getAmount());
        }
        return totalAmount;
    }

    public BigDecimal findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (checkOrdersListEmpty(sortedOrders)) return BigDecimal.ZERO;

        return new BigDecimal(sortedOrders.size());
    }

    public Book showBookDescription(String title) {
        Book searchBook = null;
        for (Book book : warehouse) {
            if (book.getTitle().equals(title)) {
                searchBook = book;
                break;
            }
        }

        if (searchBook == null) {
            throw new IllegalArgumentException("Book not found: " + title);
        }

        return searchBook;
    }

    private List<Book> findRequestedBooks() {
        return warehouse.stream()
                .filter(book -> book.getRequestCount() > 0)
                .toList();
    }

    public List<Book> showRequestedBooksSortedBy(String sortedBy) {
        List<Book> requestedBooks = findRequestedBooks();
        if (requestedBooks.isEmpty()) return Collections.emptyList();

        switch (sortedBy) {
            case "count":
                requestedBooks.sort(Comparator.comparingInt(Book::getRequestCount));
                break;
            case "title":
                requestedBooks.sort(Comparator.comparing(Book::getTitle));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortedBy);
        }

        return requestedBooks;
    }

    private List<Book> findOldBooks() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

        return warehouse.stream()
                .filter(book -> book.getReceiptDate().isBefore(sixMonthsAgo))
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();
    }

    public List<Book> showOldBooks(String sortedBy) {
        List<Book> oldBooks = findOldBooks();

        if (oldBooks.isEmpty()) return Collections.emptyList();

        switch (sortedBy) {
            case "receiptDate":
                oldBooks.sort(Comparator.comparing(Book::getReceiptDate));
                break;
            case "price":
                oldBooks.sort(Comparator.comparing(Book::getPrice));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortedBy);
        }

        return oldBooks;
    }


    // --- NEW ADDED METHODS TO WORK WITH FILES ---

    public void exportBookCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(bookData))) {
            writer.writeNext(new String[]{"id", "title", "author", "description", "price", "publicationDate", "status"});

            for (Book book : warehouse) {
                writer.writeNext(new String[]{
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getDescription(),
                        String.valueOf(book.getPrice()),
                        book.getPublicationDate().toString(),
                        book.getStatus().toString()
                });
            }

        } catch (Exception e) {
            throw new RuntimeException("Exporting books from file: " + e.getMessage());
        }
    }

    private void skipHeader(CSVReader reader) {
        try {
            reader.readNext();
        } catch (Exception e) {
            throw new RuntimeException("Skipping header:" + e.getMessage());
        }
    }

    public void importBookCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(bookData))) {
            skipHeader(reader);

            String[] row;
            while ((row = reader.readNext()) != null) {
                int id = Integer.parseInt(row[0]);
                String title = row[1];
                String author = row[2];
                String description = row[3];
                BigDecimal price = new BigDecimal(row[4]);
                LocalDate publicationDate = LocalDate.parse(row[5]);
                BookStatus status = BookStatus.valueOf(row[6]);

                Book existing = findBookById(id);

                if (existing == null) {
                    Book newBook = new Book(title, author, status, price, publicationDate, description);
                    newBook.setId(id);
                    warehouse.add(newBook);
                } else {
                    existing.setTitle(title);
                    existing.setAuthor(author);
                    existing.setPrice(price);
                    existing.setPublicationDate(publicationDate);
                    existing.setDescription(description);
                    existing.setStatus(status);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Importing books from file: " + e.getMessage());
        }
    }

    public void exportOrdersCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(orderData))) {

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
            throw new RuntimeException(e.getMessage());
        }
    }

    public void importOrdersCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(orderData))) {

            skipHeader(reader);
            String[] row;

            while ((row = reader.readNext()) != null) {
                int id = Integer.parseInt(row[0]);
                OrderStatus status = OrderStatus.valueOf(row[1]);
                LocalDate executionDate = LocalDate.parse(row[2]);
                BigDecimal amount = new BigDecimal(row[3]);
                String bookIdsString = row[4];
                int customerId = Integer.parseInt(row[5]);

                List<Book> books = new ArrayList<>();
                String[] parts = bookIdsString.split(";");
                for (String p : parts) {
                    if (!p.isEmpty()) {
                        int bookId = Integer.parseInt(p);
                        Book b = findBookById(bookId);
                        if (b != null) {
                            books.add(b);
                        }
                    }
                }

                Customer customer = findCustomerById(customerId);

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
            throw new RuntimeException("Importing orders: " + e.getMessage());
        }
    }

    public void exportCustomersCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(customerData))) {

            writer.writeNext(new String[]{"id", "name", "email"});

            for (Customer customer : customers) {
                writer.writeNext(new String[]{
                        String.valueOf(customer.getId()),
                        customer.getName(),
                        customer.getEmail()
                });
            }

        } catch (Exception e) {
            throw new RuntimeException("Exporting customers: " + e.getMessage());
        }
    }

    public void importCustomersCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(customerData))) {

            skipHeader(reader);
            String[] row;

            while ((row = reader.readNext()) != null) {
                int id = Integer.parseInt(row[0]);
                String name = row[1];
                String email = row[2];

                Customer existing = findCustomerById(id);

                if (existing == null) {
                    Customer newCustomer = new Customer(name, email);
                    newCustomer.setId(id);
                    customers.add(newCustomer);
                } else {
                    existing.setName(name);
                    existing.setEmail(email);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Importing customers: " + e.getMessage());
        }
    }
}
