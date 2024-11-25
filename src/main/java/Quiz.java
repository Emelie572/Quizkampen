import Database.QuestionsQuiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
    //Development

    private List<QuestionsQuiz> questions = new ArrayList<>();

    String playerName;
    int correctAnswers = 0;
    boolean readOnly = false;
    ScoreTable scoreTable;

    public Quiz(List<QuestionsQuiz> questions) {
        this.questions = questions;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<QuestionsQuiz> getQuestions() {
        return questions;
    }
}
