package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.bot.dto.*;
import pl.project.bot.mapper.BotMapper;
import pl.project.bot.mapper.BotSimulationMapper;
import pl.project.bot.strategy.BotBBandsStrategyService;
import pl.project.bot.strategy.BotRsiStrategyService;
import pl.project.bot.strategy.RatioService;
import pl.project.common.enums.StrategyEnum;
import pl.project.common.execDetails.ExecDetails;
import pl.project.common.execDetails.ExecDetailsHelper;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BotService {
    @Autowired
    private BotRepository botRepository;
    @Autowired
    private BotGetStockDataService botGetStockDataService;
    @Autowired
    private BotRsiStrategyService botRsiStrategyService;
    @Autowired
    private BotBBandsStrategyService botBBandsStrategyService;
    @Autowired
    private BotSimulationMapper botSimulationMapper;
    @Autowired
    private RatioService ratioService;

    public List<BotDTO> getAllBotDetails() {
        List<BotsEntity> bots = new ArrayList<>();
        botRepository.findAll().forEach(bots::add);
        return bots.stream()
                .map(BotMapper.INSTANCE::botEntityToBotDto)
                .collect(Collectors.toList());
    }

    public void addBot(BotsEntity bot) {
        botRepository.save(bot);
    }


    public void updateBot(BotsEntity bot) {
        botRepository.save(bot);
    }


    @Transactional
    public boolean deleteBot(Long id) {
        Optional<BotsEntity> result = botRepository.deleteBotsEntityById(id);
        return result.isPresent();
    }

    public ExecDetailsBot startRsiBot(@NotNull BotRsiParametersDTO parameters) {
        ExecDetailsHelper execHelper = new ExecDetailsHelper();
        execHelper.setStartDbTime(OffsetDateTime.now());
        final BotSimulationResultDto result = botRsiStrategyService.startSimulation(parameters);
        ratioService.calculateStrategyRatios(result);
        botRepository.save(botSimulationMapper
                .simulationResultToBotEntity(result, parameters, execHelper.getStartExecTime().toLocalDateTime(), StrategyEnum.RSI));
        execHelper.addNewDbTime();
        return new ExecDetailsBot(new ExecDetails(execHelper.getExecTime(), execHelper.getDbTime()), result);
    }

    public ExecDetailsBot startBBandsBot(@NotNull BotBBandsParametersDTO parameters) {
        ExecDetailsHelper execHelper = new ExecDetailsHelper();
        execHelper.setStartDbTime(OffsetDateTime.now());
        final BotSimulationResultDto result = botBBandsStrategyService.startSimulation(parameters);
        ratioService.calculateStrategyRatios(result);
        botRepository.save(botSimulationMapper
                .simulationResultToBotEntity(result, parameters, execHelper.getStartExecTime().toLocalDateTime(), StrategyEnum.BBANDS));
        execHelper.addNewDbTime();
        return new ExecDetailsBot(new ExecDetails(execHelper.getExecTime(), execHelper.getDbTime()), result);
    }

    public ExecDetailsBot getStockData(@NotNull StockDataParametersDTO parameters) {
        ExecDetailsHelper execHelper = new ExecDetailsHelper();
        execHelper.setStartDbTime(OffsetDateTime.now());
        StockDataResultDto result = botGetStockDataService.getStockData(parameters);
        execHelper.addNewDbTime();
        return new ExecDetailsBot(new ExecDetails(execHelper.getExecTime(), execHelper.getDbTime()), result);
    }
}
