package pl.project.bot.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import pl.project.bot.BotsEntity;
import pl.project.bot.dto.BotParametersDTO;
import pl.project.bot.dto.BotSimulationResultDto;
import pl.project.common.enums.StrategyEnum;
import pl.project.trade.mapper.TradeMapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class BotSimulationMapper {

    public BotsEntity simulationResultToBotEntity(@NotNull BotSimulationResultDto rsiSimulationResult,
                                                  @NotNull BotParametersDTO parameters,
                                                  @NotNull LocalDateTime createDate,
                                                  @NotNull StrategyEnum strategy) {
        final BotsEntity newBot = new BotsEntity();
        newBot.setBudget(rsiSimulationResult.getBudgetBefore());
        newBot.setResultValue(rsiSimulationResult.getBudgetAfter());
        newBot.setCreateDate(createDate);
        newBot.setStartDate(parameters.getStartDate().atStartOfDay());
        newBot.setEndDate(parameters.getEndDate().atStartOfDay());
        newBot.setStrategy(strategy.name());
        newBot.setName(parameters.getName());
        newBot.setStatus("SUCCESS");
        newBot.setPairStock(parameters.getStock());
        newBot.setResampleFreq(parameters.getResampleFreq());
        newBot.setRoi(rsiSimulationResult.getRoi());
        newBot.setWlRatio(rsiSimulationResult.getWlRatio());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            newBot.setParameters(objectMapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            newBot.setParameters(null);
        }

        newBot.setTradesById(rsiSimulationResult.getTradeList()
                .stream()
                .map(trade -> TradeMapper.INSTANCE.rsiSimulationResultToTradeEntity(trade, newBot))
                .collect(Collectors.toList()));

        return newBot;
    }
}
