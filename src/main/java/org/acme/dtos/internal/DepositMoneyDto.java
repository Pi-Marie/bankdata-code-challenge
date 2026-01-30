package org.acme.dtos.internal;

public class DepositMoneyDto {
    private Float amount;

    public DepositMoneyDto() {}

    public DepositMoneyDto(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() { return amount; }
}