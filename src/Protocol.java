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

    //TODO set this.rounds from properties.
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
//TODO Separat metod för playerStateScore hantering.
    public synchronized Quiz proccesQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
             if (playerStateCounter <2) {
                playerStateCounter++;
                return null;
            }else if (playerStateCounter >= 2){
                outputQuiz = new Quiz();
                 playerStateCounter++;
                if(playerStateCounter == 4){
                    state = ROUNDSCORE;
                }

            }
//TODO Definera dataflöde. Ändra så att setScoreMessage skickas när båda spelare har
// uppdaterat scoreTable.
        } else if (state == ROUNDSCORE) {
            if (playerStateCounter > 2) {
                scoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                playerStateCounter-=2;
                return null;
            }else if (playerStateCounter == 2) {
                scoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                inputQuiz.scoreTable =scoreTable;
                inputQuiz.readOnly = true;
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
