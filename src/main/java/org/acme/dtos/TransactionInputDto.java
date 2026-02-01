package org.acme.dtos;

public class TransactionInputDto {
    private Long toAccountId;
    private Float amount;

    public TransactionInputDto() {}

    public TransactionInputDto(Long toAccountId, Float amount) {
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Long getToAccountId() { return toAccountId; }
    public Float getAmount() { return amount; }
}