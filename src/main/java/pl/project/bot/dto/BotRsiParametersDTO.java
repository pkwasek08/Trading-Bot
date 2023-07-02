package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotRsiParametersDTO extends StockDataParametersDTO {
    private Double rsiHeightLevel;
    private Double rsiLowLevel;
    private Double budget;
    //private StrategyEnum strategy;
}
