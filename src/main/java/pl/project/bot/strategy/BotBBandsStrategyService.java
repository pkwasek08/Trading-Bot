package pl.project.bot.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Trade;
import pl.project.bot.BotGetStockDataService;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.BotBBandsParametersDTO;
import pl.project.bot.dto.BotSimulationResultDto;
import pl.project.trade.dto.TradeDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotBBandsStrategyService extends TradePositionAbstract {
    @Autowired
    private BotGetStockDataService botGetStockDataService;

    public BotSimulationResultDto startSimulation(@NotNull BotBBandsParametersDTO parameters) {
        BotSimulationResultDto stockDataResult =
                new BotSimulationResultDto(botGetStockDataService.getStockData(parameters), parameters.getBudget());
        List<TradeDTO> tradeList = new ArrayList<>();
        newBudget = parameters.getBudget();
        final BigDecimal bbandsHeightLevel = parameters.getBbandsHeightLevel() != null ?
                parameters.getBbandsHeightLevel() : BigDecimal.ZERO;
        final BigDecimal bbandsLowLevel = parameters.getBbandsLowLevel() != null ?
                parameters.getBbandsLowLevel() : BigDecimal.ZERO;
        for (final BarDTO bar : stockDataResult.getBarDataList()) {
            final BigDecimal lowerBollinger = BigDecimal.valueOf(bar.getLowerBollinger());
            final BigDecimal middleBollinger = BigDecimal.valueOf(bar.getMiddleBollinger());
            final BigDecimal upperBollinger = BigDecimal.valueOf(bar.getUpperBollinger());
            final BigDecimal openPrice = BigDecimal.valueOf(bar.getBar().getOpenPrice().doubleValue());
            final LocalDateTime openDate = bar.getBar().getBeginTime().toLocalDateTime();
            // buying
            if (lowerBollinger.add(bbandsLowLevel).compareTo(openPrice) >= 0) {
                final BigDecimal takeProfit = parameters.getTakeProfit() != null ?
                        parameters.getTakeProfit() : middleBollinger.subtract(openPrice);
                TradeDTO tradeBuy = new TradeDTO(openPrice,
                        parameters.getAmount(),
                        openDate,
                        parameters.getStopLoss(),
                        takeProfit,
                        Trade.TradeType.BUY);
                addNewTrade(tradeList, tradeBuy);
            }
            // selling
            if (upperBollinger.subtract(bbandsHeightLevel).compareTo(openPrice) <= 0) {
                final BigDecimal takeProfit = parameters.getTakeProfit() != null ?
                        parameters.getTakeProfit() : openPrice.subtract(middleBollinger);
                TradeDTO tradeSell = new TradeDTO(openPrice,
                        parameters.getAmount(),
                        openDate,
                        parameters.getStopLoss(),
                        takeProfit,
                        Trade.TradeType.SELL);
                addNewTrade(tradeList, tradeSell);
            }

            findSellPositionSlOrTpToClose(tradeList, openPrice, openDate);
            findBuyPositionSlOrTpToClose(tradeList, openPrice, openDate);
        }
        stockDataResult.setTradeList(cleanUnrealizedTrades(tradeList));
        stockDataResult.setBudgetAfter(newBudget);
        return stockDataResult;
    }
}
