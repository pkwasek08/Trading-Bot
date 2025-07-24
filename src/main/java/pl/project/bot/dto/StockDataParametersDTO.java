package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

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
        return startDate == null || endDate == null || Strings.isNotBlank(stock) || Strings.isNotBlank(resampleFreq);
    }
}
