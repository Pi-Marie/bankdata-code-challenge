package org.acme.dtos;
import org.acme.dtos.YearRateDto;
import java.util.List;

public class HistoryOutputDto {
    public Float today;
    public List<YearRateDto> history;

    public HistoryOutputDto() {
    }

    public HistoryOutputDto(Float today, List<YearRateDto> history) {
        this.today = today;
        this.history = history;
    }
}