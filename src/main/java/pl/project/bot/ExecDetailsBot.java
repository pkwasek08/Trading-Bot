package pl.project.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.execDetails.ExecDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecDetailsBot {
    private ExecDetails execDetails;
    private BotSimulationResultDto result;
}
