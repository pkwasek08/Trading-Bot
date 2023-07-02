package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.bot.dto.StockDataResultDto;
import pl.project.bot.dto.TradeDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotRsiSimulationResultDto extends StockDataResultDto {
    private Double budgetBefore;
    private Double budgetAfter;
    private List<TradeDTO> tradeList = new ArrayList<>();

    public BotRsiSimulationResultDto(StockDataResultDto stockDataResult, Double budgetBefore) {
        super(stockDataResult.getParameters(), stockDataResult.getResponse(), stockDataResult.getBarDataList());
        this.budgetBefore = budgetBefore;
    }
}
