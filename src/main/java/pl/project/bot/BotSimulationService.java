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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;

@Service
public class BotSimulationService {

    public BotSimulationResultDto startBotSimulation(String parameters) {
        BotSimulationResultDto result = new BotSimulationResultDto();
        //LocalDate startDate = LocalDate.of(2022,1,1);
        // startDate.minusDays(14);
        //  LocalDate endDate = LocalDate.of(2022,1,1);
        String apiUrl = "https://api.tiingo.com/iex/TSLA/prices?columns=open,high,low,close,volume&currency=USD&startDate=2023-01-01&endDate=2023-04-22&resampleFreq=60min&token=9a1345cb50538c0325f67ec8af5f73a2ce829314";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONArray jsonArray = new JSONArray(content.toString());
            result.setResponse(jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dateTime = jsonObject.getString("date");
                Double price = jsonObject.getDouble("close");
                double rsi = 0;
                if (i > 13) {
                    rsi = calculateRSI(jsonArray, i - 14, i);
                }
                System.out.println("Date/Time: " + dateTime + ", Price: " + price + ", RSI: " + rsi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setParameters(parameters);
        return result;
    }

    public static double calculateRSI(JSONArray array, int indexStart, int indexEnd) throws JSONException {
        BarSeries series = new BaseBarSeries();
        int period = indexEnd - indexStart;
        for (int i = indexStart; i < indexEnd; i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            BigDecimal open = BigDecimal.valueOf(jsonObject.getDouble("open"));
            BigDecimal high = BigDecimal.valueOf(jsonObject.getDouble("high"));
            BigDecimal low = BigDecimal.valueOf(jsonObject.getDouble("low"));
            BigDecimal close = BigDecimal.valueOf(jsonObject.getDouble("close"));
            BigDecimal volume = BigDecimal.valueOf(jsonObject.getDouble("volume"));
            String dateTime = jsonObject.getString("date");
            ZonedDateTime endDate = ZonedDateTime.parse(dateTime);
            Bar bar = new BaseBar(Duration.ofHours(period), endDate, open, high, low, close, volume);
            series.addBar(bar);
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, period);
        Num rsiValue = rsi.getValue(series.getEndIndex());
        return rsiValue.doubleValue();
    }
}
