package pl.project.bot.dto;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDataParametersDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String stock;
    private String resampleFreq;

    public boolean checkIsNull() {
        return startDate == null || endDate == null || Strings.isNullOrEmpty(stock) || Strings.isNullOrEmpty(resampleFreq);
    }
}
