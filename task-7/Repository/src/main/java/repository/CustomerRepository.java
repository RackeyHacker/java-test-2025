package repository;

import entity.Customer;

import java.util.List;

public interface CustomerRepository {

    List<Customer> findAll();

    void addCustomer (Customer customer);

    Customer findCustomerById (int id);

    Customer findCustomerByEmail (String email);

    void removeCustomerById (int id);

    void replaceAllCustomers(List<Customer> customers);

    void exportCustomersCSV();

    void importCustomersCSV();
}
