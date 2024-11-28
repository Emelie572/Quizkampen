import Database.QuizSource;
import Database.QuizSourceReader;
import Database.Result;
import Database.TriviaCategory;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Quiz implements Serializable {

    private String categoryString;
    private int category;
    private final List<List<String>> allQuestions = new ArrayList<>();
    private List<TriviaCategory> triviaCategories;
    private String playerName;
    private int correctAnswers = 0;
    private boolean readOnly = false;
    private ScoreTable scoreTable;
    private String playerChoosingCategory;

    public Quiz(String playerName) {
        this.readOnly = true;
        this.playerName = playerName;
    }
    public Quiz(int category,List<TriviaCategory> triviaCategories) {

        this.triviaCategories = threeRandomUnusedCategories(triviaCategories);
        this.category = category;
        QuizSourceReader quizSourceReader = new QuizSourceReader(getNumberOfQuestionsProperty(),category);
        questionMaker(quizSourceReader.getQuizSource());
        System.out.println("Quiz Class "+ quizSourceReader.getQuizSource().getResults().toString()+" Quiz Class"); //test

    }

    private List<TriviaCategory> threeRandomUnusedCategories(List<TriviaCategory> triviaCategories){

        int amountSelected = 3;
        List<TriviaCategory> threeCategories = new ArrayList<>();
        Collections.shuffle(triviaCategories);
        for (int i = 0; i < amountSelected; i++) {
            if(!triviaCategories.get(i).isUsed()){
                threeCategories.add(triviaCategories.get(i));
            }else {
                amountSelected++;
            }
        }
        return threeCategories;
    }

    private void questionMaker (QuizSource quizSource){

        for(Result result: quizSource.getResults()){
            setCategoryString(result.category);
            List<String> question = new ArrayList<>();
            question.add(result.question);
            question.addAll(shuffleAndSetCorrectAnswers(result.incorrect_answers,result.correct_answer));
            allQuestions.add(question);
        }
    }

    private List<String> shuffleAndSetCorrectAnswers(List<String> incorrect,String correct){

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

    public boolean triviaContains (String s) {

        for(TriviaCategory trivia: triviaCategories) {
            if(trivia.getName().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    public int getCategory() {
        return category;
    }
    public List<List<String>> getAllQuestions() {
        return allQuestions;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    public void addToCorrectAnswers(int Answers) {
        this.correctAnswers += Answers;
    }
    public boolean isReadOnly() {
        return readOnly;
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    public ScoreTable getScoreTable() {
        return scoreTable;
    }
    public void setScoreTable(ScoreTable scoreTable) {
        this.scoreTable = scoreTable;
    }
    public String getPlayerChoosingCategory() {
        return playerChoosingCategory;
    }
    public void setPlayerChoosingCategory(String playerChoosingCategory) {
        this.playerChoosingCategory = playerChoosingCategory;
    }
    public List<TriviaCategory> getTriviaCategories() {
        return triviaCategories;
    }
    public void setTriviaCategories(List<TriviaCategory> triviaCategories) {
        this.triviaCategories = threeRandomUnusedCategories(triviaCategories);
    }
    public String getCategoryString() {
        return categoryString;
    }
    public void setCategoryString(String categoryString) {
        this.categoryString = categoryString;
    }
}

