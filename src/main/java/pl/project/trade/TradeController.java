package pl.project.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping()
    public List<TradesEntity> getAllTrade() {
        return tradeService.getAllTrade();
    }

    @GetMapping("/{id}")
    public TradesEntity getTrade(@PathVariable Integer id) {
        return tradeService.getTrade(id);
    }

    @PostMapping()
    public void addTrade(@RequestBody TradesEntity Trade) {
        tradeService.addTrade(Trade);
    }

    @PutMapping(value = "/{id}")
    public void updateTrade(@RequestBody TradesEntity Trade, @PathVariable Integer id) {
        tradeService.updateTrade(id, Trade);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTrade(@PathVariable Integer id) {
        tradeService.deleteTrade(id);
    }

}
