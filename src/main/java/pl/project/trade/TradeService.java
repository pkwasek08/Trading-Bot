package pl.project.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;


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
