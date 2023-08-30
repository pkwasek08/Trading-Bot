package pl.project.bot.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Trade;
import pl.project.bot.BotGetStockDataService;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.BotSimulationResultDto;
import pl.project.trade.dto.TradeDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotRsiStrategyService extends TradePositionAbstract {

    @Autowired
    private BotGetStockDataService botGetStockDataService;

    public BotSimulationResultDto startSimulation(@NotNull BotRsiParametersDTO parameters) {
        BotSimulationResultDto stockDataResult =
                new BotSimulationResultDto(botGetStockDataService.getStockData(parameters), parameters.getBudget());
        List<TradeDTO> tradeList = new ArrayList<>();
        newBudget = parameters.getBudget();
        for (final BarDTO bar : stockDataResult.getBarDataList()) {
            final BigDecimal rsi = BigDecimal.valueOf(bar.getRsi());
            final BigDecimal openPrice = BigDecimal.valueOf(bar.getBar().getOpenPrice().doubleValue());
            final LocalDateTime openDate = bar.getBar().getBeginTime().toLocalDateTime();
            // buying
            if (rsi.compareTo(parameters.getRsiLowLevel()) <= 0 && bar.getBar().isBullish()) {
                TradeDTO tradeBuy = new TradeDTO(openPrice,
                        parameters.getAmount(),
                        openDate,
                        parameters.getStopLoss(),
                        parameters.getTakeProfit(),
                        Trade.TradeType.BUY);
                if (parameters.getStopLoss() == null && parameters.getTakeProfit() == null) {
                    findSellPositionAndClose(tradeList, openPrice, openDate);
                }
                addNewTrade(tradeList, tradeBuy);
            }
            // selling
            if (rsi.compareTo(parameters.getRsiHeightLevel()) >= 0 && bar.getBar().isBearish()) {
                TradeDTO tradeSell = new TradeDTO(openPrice,
                        parameters.getAmount(),
                        openDate,
                        parameters.getStopLoss(),
                        parameters.getTakeProfit(),
                        Trade.TradeType.SELL);
                if (parameters.getStopLoss() == null && parameters.getTakeProfit() == null) {
                    findBuyPositionAndClose(tradeList, openPrice, openDate);
                }
                addNewTrade(tradeList, tradeSell);
            }

            if (parameters.getStopLoss() != null && parameters.getTakeProfit() != null) {
                findSellPositionSlOrTpToClose(tradeList, openPrice, openDate);
                findBuyPositionSlOrTpToClose(tradeList, openPrice, openDate);
            }
        }
        stockDataResult.setTradeList(cleanUnrealizedTrades(tradeList));
        stockDataResult.setBudgetAfter(newBudget);
        return stockDataResult;
    }

}
