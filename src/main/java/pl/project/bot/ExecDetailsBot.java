package pl.project.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.bot.dto.StockDataResultDto;
import pl.project.execDetails.ExecDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecDetailsBot {
    private ExecDetails execDetails;
    private StockDataResultDto result;
}
