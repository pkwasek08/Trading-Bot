package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotParametersDTO extends StockDataParametersDTO {
    private String name;
    private BigDecimal budget;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private Integer amount;
}
