import java.io.FileInputStream;
import java.util.Properties;

public class Protocol {

    private final int SENDINGQUIZ = 0;
    private final int ROUNDSCORE = 1;
    private final int GAMEEND = 2;
    private int state = SENDINGQUIZ;

    private int rounds;
    private int roundsCounter = 0;
    private int playerRequestCounter = 0;
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

             if (playerRequestCounter ==0) {
                playerRequestCounter++;
                return null;
            }else if (playerRequestCounter == 1){
                outputQuiz = new Quiz();
                 playerRequestCounter++;
                if(playerRequestCounter == 2){
                    state = ROUNDSCORE;
                }

            }
//TODO Definera dataflöde. Ändra så att setScoreMessage skickas när båda spelare har
// uppdaterat scoreTable.
        } else if (state == ROUNDSCORE) {
            if (playerRequestCounter == 2) {
                scoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                playerRequestCounter -=2;
                return null;
            }else if (playerRequestCounter == 0) {


                scoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                inputQuiz.scoreTable =scoreTable;
                String stringScore = String.valueOf(inputQuiz.scoreTable.getMapScores());
                System.out.println(stringScore + " Protocol Print");
                inputQuiz.readOnly = true;
                outputQuiz = inputQuiz;
                //playerRequestCounter--;
                roundsCounter++;
                if (rounds==roundsCounter){
                    state = GAMEEND;
                } else if(playerRequestCounter == 0) {
                    state = SENDINGQUIZ;
                }
            }
        }else if (state == GAMEEND) {
            System.out.println("Game ended");
           while (true){}
        }
        return outputQuiz;
    }

}
