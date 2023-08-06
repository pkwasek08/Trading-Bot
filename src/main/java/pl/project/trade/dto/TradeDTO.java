package pl.project.trade.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ta4j.core.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeDTO {
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private Integer amount;
    private BigDecimal profitLose;
    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private String comment;
    private Trade.TradeType type;

    public TradeDTO(BigDecimal openPrice, Integer amount, LocalDateTime dateOpen, BigDecimal stopLoss,
                    BigDecimal takeProfit, Trade.TradeType type) {
        this.openPrice = openPrice;
        this.amount = amount;
        this.dateOpen = dateOpen;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.type = type;
    }
}
