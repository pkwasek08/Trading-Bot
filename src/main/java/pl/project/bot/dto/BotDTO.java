package pl.project.bot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.trade.dto.TradeDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BotDTO {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal resultValue;
    private BigDecimal budget;
    private String status;
    private String strategy;
    private String parameters;
    private LocalDateTime createDate;
    private String pairStock;
    private String resampleFreq;
    private List<TradeDTO> tradeList;
}
