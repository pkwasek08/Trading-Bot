package pl.project.bot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ta4j.core.Trade;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeDTO {
    private Double openPrice;
    private Double closePrice;
    private Integer amount;
    private Double profitLose;
    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private Double stopLoss;
    private Double takeProfit;
    private String comment;
    private Trade.TradeType type;

    public TradeDTO(Double openPrice, Integer amount, LocalDateTime dateOpen, Double stopLoss,
                    Double takeProfit, Trade.TradeType type) {
        this.openPrice = openPrice;
        this.amount = amount;
        this.dateOpen = dateOpen;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.type = type;
    }
}
