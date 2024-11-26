import java.io.FileInputStream;
import java.util.Properties;

public class Protocol {
    //Development
    private final int CONNECTION = 0;
    private final int SENDINGQUIZ = 1;
    private final int ROUNDSCORING = 2;
    private final int ENDOFGAME = 3;
    private int state = CONNECTION;

    private final int MAXROUNDS;
    private int roundsPlayed = 0;
    private boolean multiPlayerRequest = false;
    private Quiz outputQuiz;
    private final ScoreTable protcolScoreTable;
    private int chosenCategory;
    private String playerChoosingCategory;
    private boolean ChoosingCategory = true;

    //TODO Rensa upp kod.
    public Protocol() {

        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
    }
    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {

        if (state == CONNECTION) { //Connection Quiz kommer in.
            if (!multiPlayerRequest) {//Första spelarn som kopplar upp sig.
                playerChoosingCategory = inputQuiz.playerName;
                System.out.println("Category gets chosen by: " + inputQuiz.playerName);//Test.
                multiPlayerRequest = true;
                return null;
            }else {//Andra spelarn som kopplar upp sig.
                inputQuiz.playerChoosingCategory = playerChoosingCategory;
                outputQuiz = inputQuiz;
                multiPlayerRequest = false;
                state = SENDINGQUIZ;
            }
        }else if (state == SENDINGQUIZ) {
             if (!multiPlayerRequest) {
                     if(inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory)) {
                         System.out.println("Sending New Quiz requested by: " + inputQuiz.playerName);//Server sout.
                         chosenCategory = inputQuiz.category;
                         outputQuiz = new Quiz(chosenCategory);
                     }

                 multiPlayerRequest = true;
                return null;
            }else {
                     if(inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory)) {
                         System.out.println("Sending New Quiz requested by: " + inputQuiz.playerName);//Server sout.
                         chosenCategory = inputQuiz.category;
                         outputQuiz = new Quiz(chosenCategory);
                     }
                try {
                    Thread.sleep(1000); //TODO "overload". sätt till 5000.
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multiPlayerRequest = false;
                state = ROUNDSCORING;
            }
        } else if (state == ROUNDSCORING) {
            if (!multiPlayerRequest){
                ChoosingCategory = true;
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                multiPlayerRequest = true;
                System.out.println("Updating Score from player completing it first: " + inputQuiz.playerName);//Server sout.

                if((!inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
                    ChoosingCategory = false;
                    playerChoosingCategory = inputQuiz.playerName;
                }
                return null;

            }else {
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                inputQuiz.scoreTable = protcolScoreTable;
                System.out.println("Updating Score from player completing it second: " + inputQuiz.playerName);//Server sout.
                System.out.println(inputQuiz.scoreTable);//test
                inputQuiz.readOnly = true;

                if((!inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
                    ChoosingCategory = false;
                    playerChoosingCategory = inputQuiz.playerName;
                }

                System.out.println("Player Choosing Category: " + playerChoosingCategory);//Server sout.

                inputQuiz.playerChoosingCategory = playerChoosingCategory;
                outputQuiz = inputQuiz;
                multiPlayerRequest = false;
                if (MAXROUNDS == roundsPlayed){
                    state = ENDOFGAME;
                    outputQuiz.playerChoosingCategory =null;

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
