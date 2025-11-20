package com.entity;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {

    private int id;
    private static int counter = 1;
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.id = counter++;
        this.name = name;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
        if (id >= counter) {
            counter = id + 1;
        }
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
