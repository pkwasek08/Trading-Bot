package pl.project.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;


    public List<TradesEntity> getAllTrade() {
        List<TradesEntity> trades = new ArrayList<>();
        tradeRepository.findAll().forEach(trades::add);
        return trades;
    }

    public TradesEntity getTrade(Integer id) {
        return tradeRepository.findById(id).get();
    }

    public void addTrade(TradesEntity trade) {
        tradeRepository.save(trade);
    }


    public void updateTrade(Integer id, TradesEntity trade) {
        tradeRepository.save(trade);
    }


    public void deleteTrade(Integer id) {
        tradeRepository.deleteById(id);
    }
}
