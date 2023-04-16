package pl.project.bot;

import org.springframework.stereotype.Service;

@Service
public class BotSimulationService {

    public BotSimulationResultDto startBotSimulation(String parameters) {
        BotSimulationResultDto result = new BotSimulationResultDto();
        result.setParameters(parameters);
        return result;
    }
}
