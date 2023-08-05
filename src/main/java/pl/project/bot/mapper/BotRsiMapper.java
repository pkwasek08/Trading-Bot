package pl.project.bot.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.project.bot.BotsEntity;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.BotRsiSimulationResultDto;
import pl.project.common.enums.StrategyEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class BotRsiMapper {

    public BotsEntity rsiSimulationResultToBotEntity(@NotNull BotRsiSimulationResultDto rsiSimulationResult,
                                                     @NotNull BotRsiParametersDTO parameters,
                                                     @NotNull LocalDateTime createDate) {
        final BotsEntity newBot = new BotsEntity();
        newBot.setBudget(rsiSimulationResult.getBudgetBefore());
        newBot.setResultValue(rsiSimulationResult.getBudgetAfter());
        newBot.setCreateDate(createDate);
        newBot.setStartDate(parameters.getStartDate().atStartOfDay());
        newBot.setEndDate(parameters.getEndDate().atStartOfDay());
        newBot.setStrategy(StrategyEnum.RSI.name());
        newBot.setName(parameters.getName());
        newBot.setStatus("SUCCESS");
        newBot.setPairStock(parameters.getStock());
        newBot.setResampleFreq(parameters.getResampleFreq());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            newBot.setParameters(objectMapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            newBot.setParameters(null);
        }
        return newBot;
    }
}
