package repository.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import entity.Customer;
import repository.CustomerRepository;
import repository.exceptions.DataProcessingException;
import repository.util.CsvUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {

    private final String customerDataPath = "data/csv/customers.csv";
    private final List<Customer> customers = new ArrayList<>();

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public Customer findCustomerById(int id) {
        return customers.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeCustomerById(int id) {
        Customer customer = findCustomerById(id);
        customers.remove(customer);
    }

    @Override
    public void replaceAllCustomers(List<Customer> customers) {
        this.customers.clear();
        this.customers.addAll(customers);
    }

    @Override
    public void exportCustomersCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(customerDataPath))) {

            writer.writeNext(new String[]{"id", "name", "email"});

            for (Customer customer : customers) {
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
                    customers.add(newCustomer);
                } else {
                    existing.setName(name);
                    existing.setEmail(email);
                }
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error importing customers", e);
        }
    }
}
