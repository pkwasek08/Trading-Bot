package pl.project.bot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ta4j.core.Trade;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeDTO {
    Double openPrice;
    Double closePrice;
    Integer amount;
    Double profitLose;
    LocalDateTime dateOpen;
    LocalDateTime dateClose;
    Trade.TradeType type;

    public TradeDTO(Double openPrice, Integer amount, LocalDateTime dateOpen, Trade.TradeType type) {
        this.openPrice = openPrice;
        this.amount = amount;
        this.dateOpen = dateOpen;
        this.type = type;
    }
}
