package pl.project.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import pl.project.bot.BotsEntity;
import pl.project.bot.dto.BotDTO;
import pl.project.trade.TradesEntity;
import pl.project.trade.dto.TradeDTO;
import pl.project.trade.mapper.TradeMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BotMapper {
    BotMapper INSTANCE = Mappers.getMapper(BotMapper.class);

    @Mappings({
            @Mapping(target = "tradeList", expression = "java(mapTradeList(bot.getTradesById()))")
    })
    BotDTO botEntityToBotDto(BotsEntity bot);

    default List<TradeDTO> mapTradeList(Collection<TradesEntity> tradeList) {
        return tradeList.stream()
                .map(TradeMapper.INSTANCE::tradeEntityToTradeDto)
                .collect(Collectors.toList());
    }
}
