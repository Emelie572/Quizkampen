import Database.QuizSource;
import Database.QuizSourceReader;
import Database.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Quiz implements Serializable {

    public int category;
    List<List<String>> allQuestions = new ArrayList<>();
    String playerName;
    int correctAnswers = 0;
    boolean readOnly = false;
    ScoreTable scoreTable;
    String playerChoosingCategory;

public Quiz(int category,boolean requestQuiz) {
    this.category = category;
}

    public Quiz(int category) {

    this.category = category;
    QuizSourceReader quizSourceReader = new QuizSourceReader(getNumberOfQuestionsProperty(),category);
    questionMaker(quizSourceReader.getQuizSource());
    System.out.println("Quiz"+quizSourceReader.getQuizSource().getResults().toString()); //test

    }

    private void questionMaker (QuizSource quizSource){

        for(Result result: quizSource.getResults()){
            List<String> question = new ArrayList<>();
            question.add(result.question);
            question.addAll(shuffleAndSetCorrectAnswers(question,result.incorrect_answers,result.correct_answer));
            allQuestions.add(question);
        }
    }

    private List<String> shuffleAndSetCorrectAnswers(List<String> question,List<String> incorrect,String correct){
        List<String> shuffledList = new ArrayList<>(incorrect);
        shuffledList.add(correct);
        Collections.shuffle(shuffledList);
        String correctIndex ="0";

        for (int i = 0; i <shuffledList.size() ; i++) {
            if(shuffledList.get(i).equals(correct)){
                correctIndex = String.valueOf(i+1);
            }
        }

        shuffledList.add(correctIndex);
        return shuffledList;
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

    public void setCategory(int category) {
        this.category = category;
    }
}

