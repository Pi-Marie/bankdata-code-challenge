package org.acme.clients.exchangeapi;
import java.util.Map;

public class ExchangeHistoricalRateApiDto {
    public String result;
    public String base_code;
    public int year;
    public int month;
    public Map<String, Float> conversion_results;
}