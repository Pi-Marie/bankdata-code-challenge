package org.acme.dtos;

public class DepositInputDto {
    private Float amount;

    public DepositInputDto() {}

    public DepositInputDto(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() { return amount; }
}