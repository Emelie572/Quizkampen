import java.io.FileInputStream;
import java.util.Properties;

public class Protocol {

    private final int SENDINGQUIZ = 0;
    private final int ROUNDSCORE = 1;
    private final int ENDOFGAME = 2;
    private int state = SENDINGQUIZ;

    private final int rounds;
    private int roundsCounter = 0;
    private int playerRequestCounter = 0;
    private Quiz outputQuiz;
    private  ScoreTable protcolScoreTable = new ScoreTable();

    public Protocol() {
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/RoundQuestionsAmount.properties"));
        }catch(Exception e){
            System.out.println("File not found");
            e.printStackTrace();
        }
        this.rounds = Integer.parseInt(p.getProperty("rounds"));
    }
    public synchronized Quiz proccesQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
             if (playerRequestCounter ==0) {
                playerRequestCounter++;
                return null;
            }else if (playerRequestCounter == 1){
                outputQuiz = new Quiz();
                //outputQuiz.scoreTable=new ScoreTable();//test
                 playerRequestCounter++;
                if(playerRequestCounter == 2){
                    state = ROUNDSCORE;
                }
            }
        } else if (state == ROUNDSCORE) {
            if (playerRequestCounter == 2) {
                protcolScoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                playerRequestCounter --;
                return null;
            }else if (playerRequestCounter == 1) {
                protcolScoreTable.updateScoreTable(inputQuiz.playerName,inputQuiz.correctAnswers);
                inputQuiz.scoreTable.setMapScores(protcolScoreTable.getMapScores());
                System.out.println(inputQuiz.index+" quiz index"); //test
                System.out.println(inputQuiz.scoreTable.getMapScores());//test
                inputQuiz.readOnly = true;
                outputQuiz = inputQuiz;
                playerRequestCounter--;
                roundsCounter++;
                if (rounds==roundsCounter){
                    state = SENDINGQUIZ;//ENDOFGAME test
                } else if(playerRequestCounter == 0) {
                    state = SENDINGQUIZ;
                }
            }
        }else if (state == ENDOFGAME) {
            System.out.println("Game ended");
           while (true){}
        }
        return outputQuiz;
    }
}
