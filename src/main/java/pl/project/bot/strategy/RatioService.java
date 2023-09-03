package pl.project.bot.strategy;

import org.springframework.stereotype.Service;
import pl.project.bot.dto.BotSimulationResultDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RatioService {

    public void calculateStrategyRatios(final BotSimulationResultDto simulationResult) {
        simulationResult.setRoi(calculateROI(simulationResult));
        simulationResult.setWlRatio(calculateWLRatio(simulationResult));
    }

    private BigDecimal calculateROI(final BotSimulationResultDto simulationResult) {
        return simulationResult.getBudgetAfter()
                .subtract(simulationResult.getBudgetBefore())
                .divide(simulationResult.getBudgetBefore(), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateWLRatio(final BotSimulationResultDto simulationResult) {
        int tradeSize = simulationResult.getTradeList().size();
        if (tradeSize == 0) {
            return BigDecimal.ZERO;

        }
        long winTrades = simulationResult.getTradeList().stream()
                .filter(trade -> trade.getProfitLose().compareTo(BigDecimal.ZERO) >= 0)
                .count();

        return BigDecimal.valueOf((double) winTrades / tradeSize)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
