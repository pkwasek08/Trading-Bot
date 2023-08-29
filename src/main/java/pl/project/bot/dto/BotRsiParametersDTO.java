package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotRsiParametersDTO extends StockDataParametersDTO {
    private String name;
    private BigDecimal rsiHeightLevel;
    private BigDecimal rsiLowLevel;
    private BigDecimal budget;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private Integer amount;
    //private StrategyEnum strategy;
}
