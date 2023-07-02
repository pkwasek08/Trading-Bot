package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.StockDataParametersDTO;
import pl.project.bot.dto.StockDataResultDto;
import pl.project.execDetails.ExecDetails;
import pl.project.execDetails.ExecDetailsHelper;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotService {
    @Autowired
    private BotRepository botRepository;
    @Autowired
    private BotSimulationService botSimulationService;
    @Autowired
    private BotRsiService botRsiService;

    public List<BotsEntity> getAllBot() {
        List<BotsEntity> bots = new ArrayList<>();
        botRepository.findAll().forEach(bots::add);
        return bots;
    }

    public BotsEntity getBot(Integer id) {
        return botRepository.findById(id).get();
    }

    public void addBot(BotsEntity bot) {
        botRepository.save(bot);
    }


    public void updateBot(BotsEntity bot) {
        botRepository.save(bot);
    }


    public void deleteBot(Integer id) {
        botRepository.deleteById(id);
    }

    public ExecDetailsBot startRsiBot(@NotNull BotRsiParametersDTO parameters) {
        ExecDetailsHelper execHelper = new ExecDetailsHelper();
        execHelper.setStartDbTime(OffsetDateTime.now());
        StockDataResultDto result = botRsiService.startSimulation(parameters);
        // TODO save the result to DB
        execHelper.addNewDbTime();
        return new ExecDetailsBot(new ExecDetails(execHelper.getExecTime(), execHelper.getDbTime()), result);
    }

    public ExecDetailsBot getStockData(@NotNull StockDataParametersDTO parameters) {
        ExecDetailsHelper execHelper = new ExecDetailsHelper();
        execHelper.setStartDbTime(OffsetDateTime.now());
        StockDataResultDto result = botSimulationService.getStockData(parameters);
        execHelper.addNewDbTime();
        return new ExecDetailsBot(new ExecDetails(execHelper.getExecTime(), execHelper.getDbTime()), result);
    }
}
