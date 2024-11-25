import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
    //Development

    List<List<String>> allQuestions = new ArrayList<>();

    String playerName;
    int correctAnswers = 0;
    boolean readOnly = false;
    ScoreTable scoreTable;

    public Quiz(){

        List<String> question1 = new ArrayList<>(List.of("Vilken färg är solen?", "Gul", "Rosa", "1"));
        List<String> question2 = new ArrayList<>(List.of("Är äpple en frukt eller en grönsak?", "Grönsak", "Frukt", "2"));

        allQuestions.add(question1);
        allQuestions.add(question2);
    }
}
