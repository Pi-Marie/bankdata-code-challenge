package org.acme.dtos;

public class USDRateOutputDto {
    public float DKK;
    public float USD;

    public USDRateOutputDto() {
    }

    public USDRateOutputDto(float DKK, float USD) {
        this.DKK = DKK;
        this.USD = USD;
    }
}