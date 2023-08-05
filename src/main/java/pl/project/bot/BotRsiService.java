package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Trade;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.BotRsiSimulationResultDto;
import pl.project.bot.dto.TradeDTO;
import pl.project.common.helper.TradePositionHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotRsiService {

    @Autowired
    private BotGetStockDataService botGetStockDataService;

    private BigDecimal newBudget;

    public BotRsiSimulationResultDto startSimulation(@NotNull BotRsiParametersDTO parameters) {
        BotRsiSimulationResultDto stockDataResult =
                new BotRsiSimulationResultDto(botGetStockDataService.getStockData(parameters), parameters.getBudget());
        List<TradeDTO> tradeList = new ArrayList<>();
        newBudget = parameters.getBudget();
        for (final BarDTO bar : stockDataResult.getBarDataList()) {
            try {
                final BigDecimal rsi = BigDecimal.valueOf(bar.getRsi());
                final BigDecimal openPrice = BigDecimal.valueOf(bar.getBar().getOpenPrice().doubleValue());
                final LocalDateTime openDate = bar.getBar().getBeginTime().toLocalDateTime();
                // buying
                if (rsi.compareTo(parameters.getRsiLowLevel()) <= 0) {
                    TradeDTO tradeBuy = new TradeDTO(openPrice,
                            1,
                            openDate,
                            parameters.getStopLoss(),
                            parameters.getTakeProfit(),
                            Trade.TradeType.BUY);
                    if (parameters.getStopLoss() == null && parameters.getTakeProfit() == null) {
                        findSellPositionAndClose(tradeList, openPrice, openDate);
                    }
                    addNewTrade(tradeList, tradeBuy);
                    newBudget = newBudget.add(openPrice);
                }
                // selling
                if (rsi.compareTo(parameters.getRsiHeightLevel()) >= 0) {
                    TradeDTO tradeSell = new TradeDTO(openPrice,
                            1,
                            openDate,
                            parameters.getStopLoss(),
                            parameters.getTakeProfit(),
                            Trade.TradeType.SELL);
                    if (parameters.getStopLoss() == null && parameters.getTakeProfit() == null) {
                        findBuyPositionAndClose(tradeList, openPrice, openDate);
                    }
                    addNewTrade(tradeList, tradeSell);
                    newBudget = newBudget.subtract(openPrice);
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

    private void findSellPositionAndClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType() == Trade.TradeType.SELL && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(trade.getOpenPrice().subtract(currentPrice));
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(trade.getOpenPrice().subtract(currentPrice));
                });
    }

    private void findBuyPositionAndClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType() == Trade.TradeType.BUY && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(currentPrice.subtract(trade.getOpenPrice()));
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(currentPrice.subtract(trade.getOpenPrice()));
                });
    }

    private void findSellPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType().equals(Trade.TradeType.SELL) && trade.getProfitLose() == null &&
                        TradePositionHelper.checkSlAndTpForSellPosition(trade, currentPrice))
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(trade.getOpenPrice().subtract(currentPrice));
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(trade.getOpenPrice().subtract(currentPrice));
                    trade.setComment("The position has been closed because of SL or TP");
                });
    }

    private void findBuyPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType().equals(Trade.TradeType.BUY) && trade.getProfitLose() == null &&
                        TradePositionHelper.checkSlAndTpForBuyPosition(trade, currentPrice))
                .findFirst()
                .ifPresent(trade -> {
                    trade.setProfitLose(currentPrice.subtract(trade.getOpenPrice()));
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(currentPrice.subtract(trade.getOpenPrice()));
                    trade.setComment("The position has been closed because of SL or TP");
                });
    }

    private List<TradeDTO> cleanUnrealizedTrades(@NotNull List<TradeDTO> tradeList) {
        tradeList.stream()
                .filter(trade -> trade.getProfitLose() == null)
                .forEach(trade -> newBudget = newBudget.add(trade.getOpenPrice()));
        return tradeList.stream()
                .filter(trade -> trade.getProfitLose() != null)
                .collect(Collectors.toList());
    }

    private void addNewTrade(List<TradeDTO> tradeList, @NotNull TradeDTO newTrade) {
        if (newBudget.compareTo(newTrade.getOpenPrice()) >= 0) {
            tradeList.add(newTrade);
        }
    }
}
