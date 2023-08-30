package pl.project.bot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
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
public class BotGetStockDataService {

    @Value("${stock.exchange.api.token}")
    private String stockExchangeToken;
    int period = 14;
    Num k = DecimalNum.valueOf(2.0);  // Constant for belt widths

    public StockDataResultDto getStockData(@NotNull StockDataParametersDTO parameters) {
        StockDataResultDto result = new StockDataResultDto();
        result.setParameters(parameters);
        String apiUrl = "https://api.tiingo.com/iex/" + parameters.getStock() +
                "/prices?columns=open,high,low,close,volume&currency=USD&startDate="
                + parameters.getStartDate() + "&endDate=" + parameters.getEndDate() +
                "&resampleFreq=" + parameters.getResampleFreq() + "&token=" + stockExchangeToken;

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

            // RSI and BBANDS is calculated based on 14 previous bars
            for (int i = 14; i < jsonArray.length(); i++) {
                final BarSeries series = getBarSeries(jsonArray, i - 14, i, period);
                ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
                final BarDTO singleBarData = new BarDTO();
                singleBarData.setBar(barSeries.getBar(i));
                final double rsi = calculateRSI(series, closePrice);
                singleBarData.setRsi(rsi);
                final double middleBollinger = calculateMiddleBollinger(series, closePrice);
                singleBarData.setMiddleBollinger(middleBollinger);
                final double lowerBollinger = calculateLowerBollinger(series, closePrice);
                singleBarData.setLowerBollinger(lowerBollinger);
                final double upperBollinger = calculateUpperBollinger(series, closePrice);
                singleBarData.setUpperBollinger(upperBollinger);
                barDataList.add(singleBarData);
            }
            result.setBarDataList(barDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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

    private double calculateRSI(BarSeries series, ClosePriceIndicator closePrice) {
        RSIIndicator rsi = new RSIIndicator(closePrice, period);
        Num rsiValue = rsi.getValue(series.getEndIndex());
        return rsiValue.doubleValue();
    }

    private double calculateMiddleBollinger(BarSeries series, ClosePriceIndicator closePrice) {
        BollingerBandsMiddleIndicator middleBB = new BollingerBandsMiddleIndicator(closePrice);
        Num middleBBValue = middleBB.getValue(series.getEndIndex());
        return middleBBValue.doubleValue();
    }


    public double calculateUpperBollinger(BarSeries series, ClosePriceIndicator closePrice) {
        StandardDeviationIndicator sd = new StandardDeviationIndicator(closePrice, period);
        BollingerBandsMiddleIndicator middleBB = new BollingerBandsMiddleIndicator(closePrice);
        BollingerBandsUpperIndicator upperBB = new BollingerBandsUpperIndicator(middleBB, sd, k);
        Num upperBBValue = upperBB.getValue(series.getEndIndex());
        return upperBBValue.doubleValue();
    }

    public double calculateLowerBollinger(BarSeries series, ClosePriceIndicator closePrice) {
        StandardDeviationIndicator sd = new StandardDeviationIndicator(closePrice, period);
        BollingerBandsMiddleIndicator middleBB = new BollingerBandsMiddleIndicator(closePrice);
        BollingerBandsLowerIndicator lowerBB = new BollingerBandsLowerIndicator(middleBB, sd, k);
        Num lowerBBValue = lowerBB.getValue(series.getEndIndex());
        return lowerBBValue.doubleValue();
    }
}
