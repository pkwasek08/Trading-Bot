package pl.project.common.helper;

import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.trade.dto.TradeDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Helper used to check trade positions
 */
public class TradePositionHelper {

    public static boolean checkSlAndTpForSellPosition(@NotNull final TradeDTO trade, @NotNull final BigDecimal currentPrice) {
        return (trade.getStopLoss() != null && trade.getOpenPrice().add(trade.getStopLoss()).compareTo(currentPrice) < 0) ||
                (trade.getTakeProfit() != null && trade.getOpenPrice().subtract(trade.getTakeProfit()).compareTo(currentPrice) > 0);
    }

    public static boolean checkSlAndTpForBuyPosition(@NotNull final TradeDTO trade, @NotNull final BigDecimal currentPrice) {
        return (trade.getStopLoss() != null && trade.getOpenPrice().subtract(trade.getStopLoss()).compareTo(currentPrice) > 0) ||
                (trade.getTakeProfit() != null && trade.getOpenPrice().add(trade.getTakeProfit()).compareTo(currentPrice) < 0);
    }
}
