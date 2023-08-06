package pl.project.bot.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.project.trade.dto.TradeDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
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
