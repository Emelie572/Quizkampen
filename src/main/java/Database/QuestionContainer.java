package Database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestionContainer {

    @JsonProperty
    private List<QuestionsQuiz> questions;

    public List<QuestionsQuiz> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsQuiz> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "questions=" + questions +
                '}';
    }

}