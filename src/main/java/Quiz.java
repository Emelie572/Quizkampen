import Database.QuizSource;
import Database.QuizSourceReader;
import Database.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Quiz implements Serializable {
    public QuizSource quizSource;
    public int category = 11; //Defualt värde tills vidare
    List<List<String>> allQuestions = new ArrayList<>();
    String playerName;
    int correctAnswers = 0;
    boolean readOnly = false;
    ScoreTable scoreTable;
    public int response_code;
    public List<Result> results;
/*
    public Quiz() {

        List<String> question1 = new ArrayList<>(List.of("Vilken färg är solen?", "Gul", "Rosa", "1"));
        List<String> question2 = new ArrayList<>(List.of("Är äpple en frukt eller en grönsak?", "Grönsak", "Frukt", "2"));

        allQuestions.add(question1);
        allQuestions.add(question2);
    }

 */
public Quiz(int category,boolean requestQuiz) {
    this.category = category;
}

    public Quiz(int category) {

        QuizSourceReader QuizSourceReader = new QuizSourceReader(getNumberOfQuestionsProperty(),category);
        this.quizSource = QuizSourceReader.getQuizSource();
        questionMaker(quizSource);
        System.out.println("Quiz"+quizSource.results.toString()); //test

    }
    private int getNumberOfQuestionsProperty(){

        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/main/java/RoundQuestionsAmount.properties"));
        }catch(Exception e){
            System.out.println("File not found");
            e.printStackTrace();
        }
        return Integer.parseInt(p.getProperty("questions"));
    }

    private void questionMaker (QuizSource quizSource){

        for(Result result: quizSource.results){
            List<String> question = new ArrayList<>();
            question.add(result.question);
            question.addAll(result.incorrect_answers);
            question.add(result.correct_answer);
            allQuestions.add(question);
        }
    }

    public static void main(String[] args) {
        Quiz quiz = new Quiz(11);
    }
}

