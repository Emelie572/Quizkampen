import java.io.FileInputStream;
import java.util.Properties;

import Database.QuizSource;
import Database.QuizSourceReader;

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
    private final QuizSource quizSource;
    private int chosenCategory;

    //TODO Rensa upp kod.
    public Protocol() {

        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
        this.quizSource = new QuizSourceReader(0,0).getQuizSource();
    }
    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
             if (!multiPlayerRequest) {
                 chosenCategory = inputQuiz.category;
                 multiPlayerRequest = true;
                return null;
            }else {
                outputQuiz = new Quiz(chosenCategory);
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
           while (true){/*Locks program to simulate end of game, changed later.*/} //TODO Locks program to simulate end of game, changed later.
        }
        return outputQuiz;
    }

    private int getRoundProperty(){

        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/main/java/RoundQuestionsAmount.properties"));
        }catch(Exception e){
            System.out.println("File not found");
            e.printStackTrace();
        }

        return Integer.parseInt(p.getProperty("rounds"));
    }
}
