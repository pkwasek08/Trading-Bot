package pl.project.bot.strategy;

import org.ta4j.core.Trade;
import pl.project.common.helper.TradePositionHelper;
import pl.project.trade.dto.TradeDTO;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.ta4j.core.Trade.TradeType.BUY;

public abstract class TradePositionAbstract {
    BigDecimal newBudget;

    void findSellPositionAndClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
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

    void findBuyPositionAndClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
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

    void findSellPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
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

    void findBuyPositionSlOrTpToClose(@NotNull final List<TradeDTO> tradeList, @NotNull final BigDecimal currentPrice, final LocalDateTime date) {
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

    BigDecimal getFinalPrice(TradeDTO trade) {
        return trade.getOpenPrice().multiply(BigDecimal.valueOf(trade.getAmount()));
    }

    void addNewTrade(List<TradeDTO> tradeList, @NotNull TradeDTO newTrade) {
        final BigDecimal finalPrice = newTrade.getOpenPrice().multiply(BigDecimal.valueOf(newTrade.getAmount()));
        if (newBudget.compareTo(finalPrice) >= 0) {
            newBudget = newBudget.subtract(getFinalPrice(newTrade));
            tradeList.add(newTrade);
        }
    }


    List<TradeDTO> cleanUnrealizedTrades(@NotNull List<TradeDTO> tradeList) {
        tradeList.stream()
                .filter(trade -> trade.getProfitLose() == null)
                .map(this::getFinalPrice)
                .forEach(finalPrice -> newBudget = newBudget.add(finalPrice));
        return tradeList.stream()
                .filter(trade -> trade.getProfitLose() != null)
                .collect(Collectors.toList());
    }
}
