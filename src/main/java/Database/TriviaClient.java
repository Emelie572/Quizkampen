package Database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TriviaClient {

    private static final String BASE_URL = "https://opentdb.com/api.php";
    private final String CATEGORY_URL = "https://opentdb.com/api_category.php";
    private final HttpClient httpClient;
    private final Gson gson;
    private final ObjectMapper objectMapper;

    public TriviaClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        gson = new Gson();
    }

    public CategoryContainer getCategory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CATEGORY_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), CategoryContainer.class);
    }

    public QuestionContainer getQuestion(int categoryId, int amountQuestions) throws IOException, InterruptedException {
        String questionUrl = BASE_URL + "?amount=" + amountQuestions + "&category=" + categoryId;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(questionUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), QuestionContainer.class);
    }

}