package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Trade;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.BotRsiSimulationResultDto;
import pl.project.bot.dto.TradeDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotRsiService {

    @Autowired
    private BotGetStockDataService botGetStockDataService;

    private double newBudget;

    public BotRsiSimulationResultDto startSimulation(@NotNull BotRsiParametersDTO parameters) {
        BotRsiSimulationResultDto stockDataResult =
                new BotRsiSimulationResultDto(botGetStockDataService.getStockData(parameters), parameters.getBudget());
        List<TradeDTO> tradeList = new ArrayList<>();
        newBudget = parameters.getBudget();
        for (final BarDTO bar : stockDataResult.getBarDataList()) {
            try {
                final double rsi = bar.getRsi();
                final double openPrice = bar.getBar().getOpenPrice().doubleValue();
                // buying
                if (rsi <= parameters.getRsiLowLevel() && newBudget >= openPrice) {
                    TradeDTO tradeBuy = new TradeDTO(bar.getBar().getOpenPrice().doubleValue(), 1, LocalDateTime.now(), Trade.TradeType.BUY);
                    findSellPositionAndClose(tradeList, openPrice);
                    //TODO add stop loss
                    tradeList.add(tradeBuy);
                    newBudget += openPrice;

                }
                //selling
                if (rsi >= parameters.getRsiHeightLevel() && newBudget >= openPrice) {
                    TradeDTO tradeSell = new TradeDTO(bar.getBar().getOpenPrice().doubleValue(), 1, LocalDateTime.now(), Trade.TradeType.SELL);
                    findBuyPositionAndClose(tradeList, openPrice);
                    //TODO add stop loss
                    tradeList.add(tradeSell);
                    newBudget += -openPrice;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stockDataResult.setTradeList(cleanUnrealizedTrades(tradeList));
        stockDataResult.setBudgetAfter(newBudget);
        return stockDataResult;
    }

    private void findSellPositionAndClose(@NotNull List<TradeDTO> tradeList, double currentPrice) {
        tradeList.stream()
                .filter(trade -> trade.getType() == Trade.TradeType.SELL && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(trade.getOpenPrice() - currentPrice);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(LocalDateTime.now());
                    newBudget += trade.getOpenPrice() - currentPrice;
                });
    }

    private void findBuyPositionAndClose(@NotNull List<TradeDTO> tradeList, double currentPrice) {
        tradeList.stream()
                .filter(trade -> trade.getType() == Trade.TradeType.BUY && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(currentPrice - trade.getOpenPrice());
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(LocalDateTime.now());
                    newBudget += currentPrice - trade.getOpenPrice();
                });
    }

    private List<TradeDTO> cleanUnrealizedTrades(@NotNull List<TradeDTO> tradeList) {
        tradeList.stream()
                .filter(trade -> trade.getProfitLose() == null)
                .forEach(trade -> newBudget += trade.getOpenPrice());
        return tradeList.stream()
                .filter(trade -> trade.getProfitLose() != null)
                .collect(Collectors.toList());
    }
}
