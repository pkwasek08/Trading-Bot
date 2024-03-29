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
public class BotRsiParametersDTO extends BotParametersDTO {
    private BigDecimal rsiHeightLevel;
    private BigDecimal rsiLowLevel;
}
