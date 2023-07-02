package pl.project.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.project.bot.dto.BotRsiParametersDTO;
import pl.project.bot.dto.StockDataParametersDTO;

import java.util.List;

@RestController
@RequestMapping(value = "/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @GetMapping()
    public List<BotsEntity> getAllBot() {
        return botService.getAllBot();
    }

    @GetMapping("/{id}")
    public BotsEntity getBot(@PathVariable Integer id) {
        return botService.getBot(id);
    }

    @PostMapping()
    public void addBot(@RequestBody BotsEntity Bot) {
        botService.addBot(Bot);
    }

    @PutMapping()
    public void updateBot(@RequestBody BotsEntity bot) {
        botService.updateBot(bot);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBot(@PathVariable Integer id) {
        botService.deleteBot(id);
    }

    @GetMapping(value = "/startRsiBot")
    public ResponseEntity<Object> startRsiBot(@RequestBody BotRsiParametersDTO parameters) {
        if (parameters == null || parameters.checkIsNull()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ValidationException - empty parameters");
        }
        return ResponseEntity.ok(botService.startRsiBot(parameters));
    }

    @GetMapping(value = "/stockData")
    public ResponseEntity<Object> getStockData(@RequestBody StockDataParametersDTO parameters) {
        if (parameters == null || parameters.checkIsNull()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ValidationException - empty parameters");
        }
        return ResponseEntity.ok(botService.getStockData(parameters));
    }

}
