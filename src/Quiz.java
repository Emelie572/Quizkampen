import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {

    List<List<String>> allQuestions = new ArrayList<>();

    String playerName;
    int correctAnswers = 0;
    int index = 0;

    private ScoreTable scoreTable;

    private boolean answerOrReadQuestions = true;

    public Quiz(){

        List<String> question1 = new ArrayList<>(List.of("Vilken färg är solen?", "Gul", "Rosa", "0"));
        List<String> question2 = new ArrayList<>(List.of("Är äpple en frukt eller en grönsak?", "Grönsak", "Frukt", "1"));

        allQuestions.add(question1);
        allQuestions.add(question2);

    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getIndex() {
        return index;
    }
}
