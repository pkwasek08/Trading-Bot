package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Trade;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.BotRsiSimulationResultDto;
import pl.project.bot.dto.TradeDTO;
import pl.project.helper.TradePositionHelper;

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
                final LocalDateTime openDate = bar.getBar().getBeginTime().toLocalDateTime();
                // buying
                if (rsi <= parameters.getRsiLowLevel() && newBudget >= openPrice) {
                    TradeDTO tradeBuy = new TradeDTO(openPrice,
                            1,
                            openDate,
                            parameters.getStopLoss(),
                            parameters.getTakeProfit(),
                            Trade.TradeType.BUY);
                    if (parameters.getStopLoss() == null && parameters.getTakeProfit() == null) {
                        findSellPositionAndClose(tradeList, openPrice, openDate);
                    }
                    tradeList.add(tradeBuy);
                    newBudget += openPrice;
                }
                // selling
                if (rsi >= parameters.getRsiHeightLevel() && newBudget >= openPrice) {
                    TradeDTO tradeSell = new TradeDTO(openPrice,
                            1,
                            openDate,
                            parameters.getStopLoss(),
                            parameters.getTakeProfit(),
                            Trade.TradeType.SELL);
                    if (parameters.getStopLoss() == null && parameters.getTakeProfit() == null) {
                        findBuyPositionAndClose(tradeList, openPrice, openDate);
                    }
                    tradeList.add(tradeSell);
                    newBudget += -openPrice;
                }

                if (parameters.getStopLoss() != null && parameters.getTakeProfit() != null) {
                    findSellPositionSlOrTpToClose(tradeList, openPrice, openDate);
                    findBuyPositionSlOrTpToClose(tradeList, openPrice, openDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stockDataResult.setTradeList(cleanUnrealizedTrades(tradeList));
        stockDataResult.setBudgetAfter(newBudget);
        return stockDataResult;
    }

    private void findSellPositionAndClose(@NotNull final List<TradeDTO> tradeList, final double currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType() == Trade.TradeType.SELL && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(trade.getOpenPrice() - currentPrice);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget += trade.getOpenPrice() - currentPrice;
                });
    }

    private void findBuyPositionAndClose(@NotNull final List<TradeDTO> tradeList, final double currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType() == Trade.TradeType.BUY && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(currentPrice - trade.getOpenPrice());
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget += currentPrice - trade.getOpenPrice();
                });
    }

    private void findSellPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, final double currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType().equals(Trade.TradeType.SELL) && trade.getProfitLose() == null &&
                        TradePositionHelper.checkSlAndTpForSellPosition(trade, currentPrice))
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(trade.getOpenPrice() - currentPrice);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget += trade.getOpenPrice() - currentPrice;
                    trade.setComment("The position has been closed because of SL or TP");
                });
    }

    private void findBuyPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, final double currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType().equals(Trade.TradeType.BUY) && trade.getProfitLose() == null &&
                        TradePositionHelper.checkSlAndTpForBuyPosition(trade, currentPrice))
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(currentPrice - trade.getOpenPrice());
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget += currentPrice - trade.getOpenPrice();
                    trade.setComment("The position has been closed because of SL or TP");
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
