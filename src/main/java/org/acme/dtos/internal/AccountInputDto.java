package org.acme.dtos.internal;

public class AccountInputDto {

    private String firstName;
    private String lastName;

    public AccountInputDto() {}

    public AccountInputDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    // Setters, do I need these?
    public void setFirstName(String name) { this.firstName = name; }
    public void setLastName(String name) { this.lastName = name; }
}
