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
public class BotSimulationResultDto extends StockDataResultDto {
    private BigDecimal budgetBefore;
    private BigDecimal budgetAfter;
    private BigDecimal roi;
    private BigDecimal wlRatio;
    private List<TradeDTO> tradeList = new ArrayList<>();

    public BotSimulationResultDto(StockDataResultDto stockDataResult, BigDecimal budgetBefore) {
        super(stockDataResult.getParameters(), stockDataResult.getBarDataList());
        this.budgetBefore = budgetBefore;
    }
}
