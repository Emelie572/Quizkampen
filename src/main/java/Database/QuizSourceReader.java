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
                    //BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    Scanner sc = new Scanner(new InputStreamReader(url.openStream()));
                    BufferedWriter bw = Files.newBufferedWriter(p)
            ) {
                String temp;

                sc.useDelimiter(",\"");
                while (sc.hasNext()) {//TODO tabort, är till för att göra .json filen lättläslig.

                    temp = sc.next();
                    temp = temp.replaceAll("&#039","'");
                    //temp = temp.replaceAll("&quot;","\"");
                    bw.write(temp+",");
                    bw.newLine();
                    bw.write("\"");
                }

                //temp = br.readLine();
                //bw.write(temp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                this.quizSource = mapper.readValue(p.toFile(), QuizSource.class);
                System.out.println("QuizSourceReader" +quizSource.results.toString()); //test

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
/*
    public static void main(String[] args) {
        QuizSourceReader reader = new QuizSourceReader(3,11);
    }

 */
}