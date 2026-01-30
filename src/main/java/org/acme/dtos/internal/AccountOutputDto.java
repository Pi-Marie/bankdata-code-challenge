package org.acme.dtos.internal;

public class AccountOutputDto {

    private float balance;
    private String firstName;
    private String lastName;

    public AccountOutputDto() {}

    public AccountOutputDto(float balance, String firstName, String lastName) {
        this.balance = balance;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public float getBalance() { return balance; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    // Setters, do I need these?
    public void setFirstName(String name) { this.firstName = name; }
    public void setLastName(String name) { this.lastName = name; }
}
