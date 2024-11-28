package Database;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class QuizSourceReader {

    private QuizSource quizSource;

    public QuizSourceReader(int quizAmmountInput,int quizCategoryInput )  {

        String quizConfig;
        int quizAmmount = quizAmmountInput;
        int quizCategory = quizCategoryInput;
        quizConfig = "https://opentdb.com/api.php?amount="+quizAmmount+"&category="+quizCategory+"&difficulty=easy&type=multiple";

        try {

            URL url = new URI(quizConfig).toURL();
            Path p = Paths.get("src/main/java/Database/QuizSource.json");

            try (
                    //BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream())); //se to-do
                    Scanner sc = new Scanner(new InputStreamReader(url.openStream()));
                    BufferedWriter bw = Files.newBufferedWriter(p)
            ) {
                String temp;

                sc.useDelimiter(",\"");
                while (sc.hasNext()) {

                    temp = sc.next();
                    bw.write(temp+",");
                    bw.newLine();
                    bw.write("\"");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                this.quizSource = mapper.readValue(p.toFile(), QuizSource.class);
                //System.out.println("QuizSourceReader" +quizSource.results.toString()); //test

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public QuizSource getQuizSource() {
        return quizSource;
    }
}