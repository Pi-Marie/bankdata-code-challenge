package org.acme.dtos;

public class AccountInputDto {

    private String firstName;
    private String lastName;

    public AccountInputDto() {}

    public AccountInputDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
