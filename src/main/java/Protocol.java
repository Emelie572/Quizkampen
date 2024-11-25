import Database.QuestionContainer;
import Database.QuestionsQuiz;
import Database.TriviaClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Protocol {
    //Development
    private final int SENDINGQUIZ = 0;
    private final int ROUNDSCORING = 1;
    private final int ENDOFGAME = 2;
    private int state = SENDINGQUIZ;

    private final int MAXROUNDS;
    private int roundsPlayed = 0;
    private boolean multiPlayerRequest = false;
    private Quiz outputQuiz;
    private final ScoreTable protcolScoreTable;

    TriviaClient triviaClient;
    List<QuestionsQuiz> questions;

    //TODO Rensa upp kod.
    public Protocol() throws IOException, InterruptedException {

        this.triviaClient = new TriviaClient();
        this.questions = new ArrayList<>();

        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);

        databaseQuestions();
    }

    public void databaseQuestions() {
        try {
            QuestionContainer questionContainer = triviaClient.getQuestion(9,2);
            for (QuestionsQuiz question : questionContainer.getQuestions()) {
                String quizQuestion  = question.getQuestion();
                String correctAnswer = question.getCorrectAnswer();
                List<String> alternetivesList = new ArrayList<>(question.getAlternatives());
                alternetivesList.remove(correctAnswer);
                questions.add(new QuestionsQuiz(quizQuestion,alternetivesList,correctAnswer));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
             if (!multiPlayerRequest) {
                 multiPlayerRequest = true;
                return null;
            }else {
                outputQuiz = new Quiz(questions);
                multiPlayerRequest = false;
                state = ROUNDSCORING;
            }
        } else if (state == ROUNDSCORING) {
            if (!multiPlayerRequest){
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                multiPlayerRequest = true;
                return null;
            }else {
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                inputQuiz.scoreTable = protcolScoreTable;
                System.out.println(inputQuiz.scoreTable);//test
                inputQuiz.readOnly = true;
                outputQuiz = inputQuiz;
                multiPlayerRequest = false;
                if (MAXROUNDS == roundsPlayed){
                    state = ENDOFGAME;
                } else {
                    state = SENDINGQUIZ;
                }
            }
        }else if (state == ENDOFGAME) {
            System.out.println("Game ended");
           while (true){/*Locks program to simulate end of game, changed later.*/}
        }
        return outputQuiz;
    }

    private int getRoundProperty(){

        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/RoundQuestionsAmount.properties"));
        }catch(Exception e){
            System.out.println("File not found");
            e.printStackTrace();
        }

        return Integer.parseInt(p.getProperty("rounds"));
    }
}
