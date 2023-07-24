package pl.project.helper;

import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.TradeDTO;

/**
 * Helper used to check trade positions
 */
public class TradePositionHelper {

    public static boolean checkSlAndTpForSellPosition(final TradeDTO trade, final double currentPrice) {
        return (trade.getStopLoss() != null && trade.getOpenPrice() + trade.getStopLoss() < currentPrice) ||
                (trade.getTakeProfit() != null && trade.getOpenPrice() - trade.getTakeProfit() > currentPrice);
    }

    public static boolean checkSlAndTpForBuyPosition(final TradeDTO trade, final double currentPrice) {
        return (trade.getStopLoss() != null && trade.getOpenPrice() - trade.getStopLoss() > currentPrice) ||
                (trade.getTakeProfit() != null && trade.getOpenPrice() + trade.getTakeProfit() < currentPrice);
    }

    public static boolean checkSlTpParameters(final BotRsiParametersDTO parameters) {
        return (parameters.getStopLoss() == null && parameters.getTakeProfit() != null) ||
                (parameters.getStopLoss() != null && parameters.getTakeProfit() == null);
    }
}
