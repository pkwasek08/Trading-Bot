package pl.project.bot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import pl.project.bot.dto.BarDTO;
import pl.project.bot.dto.StockDataParametersDTO;
import pl.project.bot.dto.StockDataResultDto;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotSimulationService {

    public StockDataResultDto getStockData(@NotNull StockDataParametersDTO parameters) {
        StockDataResultDto result = new StockDataResultDto();
        result.setParameters(parameters);
        String apiUrl = "https://api.tiingo.com/iex/" + parameters.getStock() +
                "/prices?columns=open,high,low,close,volume&currency=USD&startDate="
                + parameters.getStartDate() + "&endDate=" + parameters.getEndDate() +
                "&resampleFreq=" + parameters.getResampleFreq() + "&token=9a1345cb50538c0325f67ec8af5f73a2ce829314";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONArray jsonArray = new JSONArray(content.toString());
            result.setResponse(jsonArray.toString());
            BarSeries barSeries = getBarSeries(jsonArray, 0, jsonArray.length(), null);
            List<BarDTO> barDataList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dateTime = jsonObject.getString("date");
                Double price = jsonObject.getDouble("close");
                double rsi = 0;
                // RSI is calculated based on 14 previous bars
                if (i > 13) {
                    rsi = calculateRSI(jsonArray, i - 14, i);
                    BarDTO singleBarData = new BarDTO();
                    singleBarData.setBar(barSeries.getBar(i));
                    singleBarData.setRsi(rsi);
                    barDataList.add(singleBarData);
                }
                // assign new bar with calculated rsi
                System.out.println("Date/Time: " + dateTime + ", Price: " + price + ", RSI: " + rsi);
            }
            result.setBarDataList(barDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private double calculateRSI(JSONArray array, int indexStart, int indexEnd) throws JSONException {
        int period = indexEnd - indexStart;
        BarSeries series = getBarSeries(array, indexStart, indexEnd, period);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, period);
        Num rsiValue = rsi.getValue(series.getEndIndex());
        return rsiValue.doubleValue();
    }

    private BarSeries getBarSeries(JSONArray array, int indexStart, int indexEnd, Integer period) throws JSONException {
        BarSeries series = new BaseBarSeries();
        Duration duration = period != null ? Duration.ofHours(period) : Duration.ZERO;
        for (int i = indexStart; i < indexEnd; i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            BigDecimal open = BigDecimal.valueOf(jsonObject.getDouble("open"));
            BigDecimal high = BigDecimal.valueOf(jsonObject.getDouble("high"));
            BigDecimal low = BigDecimal.valueOf(jsonObject.getDouble("low"));
            BigDecimal close = BigDecimal.valueOf(jsonObject.getDouble("close"));
            BigDecimal volume = BigDecimal.valueOf(jsonObject.getDouble("volume"));
            String dateTime = jsonObject.getString("date");
            ZonedDateTime endDate = ZonedDateTime.parse(dateTime);
            final Bar bar = new BaseBar(duration, endDate, open, high, low, close, volume);
            series.addBar(bar);
        }
        return series;
    }
}
