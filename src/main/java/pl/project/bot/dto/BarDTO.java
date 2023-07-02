package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ta4j.core.Bar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarDTO {
    private Bar bar;
    private double rsi;
}
