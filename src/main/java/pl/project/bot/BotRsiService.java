package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Trade;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.BotRsiSimulationResultDto;
import pl.project.trade.dto.TradeDTO;
import pl.project.common.helper.TradePositionHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ta4j.core.Trade.TradeType.BUY;

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
                if (rsi.compareTo(parameters.getRsiLowLevel()) <= 0 && bar.getBar().isBullish()) {
                    TradeDTO tradeBuy = new TradeDTO(openPrice,
                            parameters.getAmount(),
                            openDate,
                            parameters.getStopLoss(),
                            parameters.getTakeProfit(),
                            BUY);
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
                    final BigDecimal profitLose = trade.getOpenPrice()
                            .subtract(currentPrice)
                            .multiply(BigDecimal.valueOf(trade.getAmount()));
                    trade.setProfitLose(profitLose);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(getFinalPrice(trade).add(profitLose));
                });
    }

    private void findBuyPositionAndClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType() == BUY && trade.getProfitLose() == null)
                .findFirst()
                .ifPresent(trade -> {
                    final BigDecimal profitLose = currentPrice
                            .subtract(trade.getOpenPrice())
                            .multiply(BigDecimal.valueOf(trade.getAmount()));
                    trade.setProfitLose(profitLose);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(getFinalPrice(trade).add(profitLose));
                });
    }

    private void findSellPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType().equals(Trade.TradeType.SELL) && trade.getProfitLose() == null &&
                        TradePositionHelper.checkSlAndTpForSellPosition(trade, currentPrice))
                .findFirst()
                .ifPresent(trade -> {
                    final BigDecimal profitLose = trade.getOpenPrice()
                            .subtract(currentPrice)
                            .multiply(BigDecimal.valueOf(trade.getAmount()));
                    trade.setProfitLose(profitLose);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(getFinalPrice(trade).add(profitLose));
                    trade.setComment("The position has been closed because of SL or TP");
                });
    }

    private void findBuyPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
        tradeList.stream()
                .filter(trade -> trade.getType().equals(BUY) && trade.getProfitLose() == null &&
                        TradePositionHelper.checkSlAndTpForBuyPosition(trade, currentPrice))
                .findFirst()
                .ifPresent(trade -> {
                    final BigDecimal profitLose = currentPrice
                            .subtract(trade.getOpenPrice())
                            .multiply(BigDecimal.valueOf(trade.getAmount()));
                    trade.setProfitLose(profitLose);
                    trade.setClosePrice(currentPrice);
                    trade.setDateClose(date);
                    newBudget = newBudget.add(getFinalPrice(trade).add(profitLose));
                    trade.setComment("The position has been closed because of SL or TP");
                });
    }

    private void addNewTrade(List<TradeDTO> tradeList, @NotNull TradeDTO newTrade) {
        final BigDecimal finalPrice = newTrade.getOpenPrice().multiply(BigDecimal.valueOf(newTrade.getAmount()));
        if (newBudget.compareTo(finalPrice) >= 0) {
            newBudget = newBudget.subtract(getFinalPrice(newTrade));
            tradeList.add(newTrade);
        }
    }


    private List<TradeDTO> cleanUnrealizedTrades(@NotNull List<TradeDTO> tradeList) {
        tradeList.stream()
                .filter(trade -> trade.getProfitLose() == null)
                .map(this::getFinalPrice)
                .forEach(finalPrice -> newBudget = newBudget.add(finalPrice));
        return tradeList.stream()
                .filter(trade -> trade.getProfitLose() != null)
                .collect(Collectors.toList());
    }

    private BigDecimal getFinalPrice(TradeDTO trade) {
        return trade.getOpenPrice().multiply(BigDecimal.valueOf(trade.getAmount()));
    }
}
