package Database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuestionsQuiz {

    @JsonProperty
    private String question;

    @JsonProperty
    private String correctAnswer;

    @JsonProperty
    private List<String> alternatives;

    public QuestionsQuiz(String question, List<String> wrongAnswers, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;

        this.alternatives = new ArrayList<>(wrongAnswers);
        this.alternatives.add(correctAnswer);

        Collections.shuffle(this.alternatives);

    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return question + "\nAlternativ: " + alternatives;

    }
}


