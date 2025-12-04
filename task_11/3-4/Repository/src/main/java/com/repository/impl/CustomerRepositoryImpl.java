package com.repository.impl;

import com.annotations.Repository;
import com.config.dbconfig.DBConnectionManager;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.entity.Customer;
import com.repository.CustomerRepository;
import com.repository.exceptions.DataProcessingException;
import com.repository.util.CsvUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final String customerDataPath = "data/csv/customers.csv";

    private final DBConnectionManager connectionManager = new DBConnectionManager();

    public CustomerRepositoryImpl() {
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String query = "select * from customers";
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                customers.add(setCustomerParameters(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    private Customer setCustomerParameters(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        return customer;
    }

    @Override
    public void addCustomer(Customer customer) {
        String query = "insert into customers (name, email) values (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setId(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer findCustomerById(int id) {
        String query = "select * from customers where customer_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return setCustomerParameters(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        String query = "select * from customers where email = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return setCustomerParameters(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeCustomerById(int id) {
        String query = "delete from customers where customer_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceAllCustomers(List<Customer> customers) {
        String truncateQuery = "truncate table customers restart identity cascade";
        String insertQuery = "insert into customers (name, email) values (?, ?)";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(truncateQuery);
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (Customer customer : customers) {
                    preparedStatement.setString(1, customer.getName());
                    preparedStatement.setString(2, customer.getEmail());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportCustomersCSV() {
        List<Customer> allCustomers = findAll();
        try (CSVWriter writer = new CSVWriter(new FileWriter(customerDataPath))) {
            writer.writeNext(new String[]{"customer_id", "name", "email"});
            for (Customer customer : allCustomers) {
                writer.writeNext(new String[]{
                        String.valueOf(customer.getId()),
                        customer.getName(),
                        customer.getEmail()
                });
            }
        } catch (Exception e) {
            throw new DataProcessingException("Error exporting customers", e);
        }
    }

    @Override
    public void importCustomersCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(customerDataPath))) {
            CsvUtils.skipHeader(reader);
            String[] row;
            while ((row = reader.readNext()) != null) {
                int id = Integer.parseInt(row[0]);
                String name = row[1];
                String email = row[2];
                Customer existing = findCustomerById(id);
                if (existing == null) {
                    Customer newCustomer = new Customer(name, email);
                    newCustomer.setId(id);
                    addCustomer(newCustomer);
                } else {
                    existing.setName(name);
                    existing.setEmail(email);
                    String updateQuery = "update customers set name = ?, email = ? where customer_id = ?";
                    try (Connection connection = connectionManager.getConnection();
                         PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, existing.getName());
                        preparedStatement.setString(2, existing.getEmail());
                        preparedStatement.setInt(3, existing.getId());
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            throw new DataProcessingException("Error importing customers", e);
        }
    }
}
