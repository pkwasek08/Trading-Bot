package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotBBandsParametersDTO extends BotParametersDTO {
    private BigDecimal bbandsHeightLevel;
    private BigDecimal bbandsLowLevel;
}
