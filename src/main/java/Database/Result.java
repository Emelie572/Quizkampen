package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {
    public String type;
    public String difficulty;
    public String category;
    public String question;
    public String correct_answer;
    public List<String> incorrect_answers;

    @Override
    public String toString() {
        return "Result{" +
                "type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", correct_answer='" + correct_answer + '\'' +
                ", incorrect_answers=" + incorrect_answers +
                '}';
    }
}