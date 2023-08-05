package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotRsiSimulationResultDto extends StockDataResultDto {
    private BigDecimal budgetBefore;
    private BigDecimal budgetAfter;
    private List<TradeDTO> tradeList = new ArrayList<>();

    public BotRsiSimulationResultDto(StockDataResultDto stockDataResult, BigDecimal budgetBefore) {
        super(stockDataResult.getParameters(), stockDataResult.getResponse(), stockDataResult.getBarDataList());
        this.budgetBefore = budgetBefore;
    }
}
