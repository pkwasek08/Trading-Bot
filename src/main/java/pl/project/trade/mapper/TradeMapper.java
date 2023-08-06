package pl.project.trade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import pl.project.bot.BotsEntity;
import pl.project.trade.dto.TradeDTO;
import pl.project.trade.TradesEntity;

import javax.validation.constraints.NotNull;

@Mapper
public interface TradeMapper {
    TradeMapper INSTANCE = Mappers.getMapper(TradeMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "bot", target = "bot")
    })
    TradesEntity rsiSimulationResultToTradeEntity(@NotNull TradeDTO trade, @NotNull BotsEntity bot);

    TradeDTO tradeEntityToTradeDto(@NotNull TradesEntity trade);
}
