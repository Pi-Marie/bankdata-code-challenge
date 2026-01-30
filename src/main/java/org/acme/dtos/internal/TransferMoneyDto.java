package org.acme.dtos.internal;

public class TransferMoneyDto {
    private Long toAccountId;
    private Float amount;

    public TransferMoneyDto() {}

    public TransferMoneyDto(Long toAccountId, Float amount) {
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Long getToAccountId() { return toAccountId; }
    public Float getAmount() { return amount; }
}