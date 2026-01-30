package org.acme.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;

@Entity
public class Account { 
    @Id
    @GeneratedValue
    private Long id;

    private float balance;

    private String firstName;

    private String lastName;

    public Account() {} // is this necessary??

    public Account(String firstName, String lastName) {
        this.balance = 0f;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getters
    public Long getId() { return id; }
    public float getBalance() { return balance; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    // setters
    public void setFirstName(String name) { this.firstName = name; }
    public void setLastName(String name) { this.lastName = name; }
    public void setBalance(float balance) { this.balance = balance; }
}