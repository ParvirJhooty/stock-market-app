import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class StockSearch {
    private final String apiKey;
    private final String baseURL = "https://www.alphavantage.co/query";

    public StockSearch(String apiKey) {
        this.apiKey = apiKey;
    }

    public JSONObject getStockInfo(String symbol) {
        String url = baseURL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // Handle error
            return null;
        }

        JSONObject json = new JSONObject(response.body());
        JSONObject globalQuote = json.optJSONObject("Global Quote");
        if (globalQuote == null) {
            return null;
        }

        JSONObject result = new JSONObject();
        result.put("symbol", globalQuote.optString("01. symbol"));
        result.put("open", globalQuote.optString("02. open"));
        result.put("high", globalQuote.optString("03. high"));
        result.put("low", globalQuote.optString("04. low"));
        result.put("price", globalQuote.optString("05. price"));
        result.put("volume", globalQuote.optString("06. volume"));
        result.put("latest_trading_day", globalQuote.optString("07. latest trading day"));
        result.put("previous_close", globalQuote.optString("08. previous close"));
        result.put("change", globalQuote.optString("09. change"));
        result.put("change_percent", globalQuote.optString("10. change percent"));

        return result;
    }
}