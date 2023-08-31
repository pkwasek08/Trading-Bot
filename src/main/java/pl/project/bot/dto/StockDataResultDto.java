package pl.project.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDataResultDto {
    private StockDataParametersDTO parameters;
    private List<BarDTO> barDataList;
}
