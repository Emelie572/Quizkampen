import java.io.FileInputStream;
import java.util.Properties;

public class Protocol {

    private final int SENDINGQUIZ = 0;
    private final int ROUNDSCORE = 1;
    private final int GAMEEND = 2;
    private int state = SENDINGQUIZ;

    private int rounds;
    private int roundsCounter = 0;
    private int playerStateCounter = 0;
    private Quiz outputQuiz;
    private ScoreTable scoreTable = new ScoreTable();

    public Protocol() {
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/RoundQuestionsAmount.properties"));
        }catch(Exception e){
            System.out.println("File not found");
            e.printStackTrace();
        }
        this.rounds = Integer.parseInt(p.getProperty("rounds","2"));
    }
//TODO Överväg separat metod för playerStateCounter.
    public synchronized Quiz proccesQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
            if (playerStateCounter == 0) {
                playerStateCounter++;
                return null;
            }else if (playerStateCounter == 1){
                outputQuiz = new Quiz();
                playerStateCounter++;
                state = ROUNDSCORE;
            }
//TODO refaktorera playerName, correctAnswers, scoreTable och readOnly med getter och setter.

        } else if (state == ROUNDSCORE) {
            if (playerStateCounter == 2) {
                scoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                playerStateCounter--;
                return null;
            }else if (playerStateCounter == 1) {
                scoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                inputQuiz.setScoreMessage(scoreTable);
                inputQuiz.readOnly(true);
                outputQuiz = inputQuiz;
                playerStateCounter--;
                roundsCounter++;
                if (rounds==roundsCounter){
                    state = GAMEEND;
                } else {
                    state = SENDINGQUIZ;
                }
            }
        }else if (state == GAMEEND) {
            System.exit(0);
        }
        return outputQuiz;
    }

}
