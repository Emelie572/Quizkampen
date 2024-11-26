import java.io.FileInputStream;
import java.util.Properties;

public class Protocol {

    private final int CONNECTION = 0;
    private final int SENDINGQUIZ = 1;
    private final int ROUNDSCORING = 2;
    private final int ENDOFGAME = 3;
    private int state = CONNECTION;

    private final int MAXROUNDS;
    private final ScoreTable protcolScoreTable;
    private int roundsPlayed = 0;

    private Quiz outputQuiz;
    private String playerChoosingCategory;
    private boolean multiPlayerRequest = false;
    private boolean ChoosingCategory = true;

    //TODO Rensa upp kod.
    public Protocol() {

        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
    }

    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {

        if (state == CONNECTION) {//Connection Quiz kommer in.

            if (!multiPlayerRequest) {                          //Första spelarn som kopplar upp sig.
                playerChoosingCategory = inputQuiz.playerName;  //och markeras för att välja kategori
                System.out.println("First Category gets chosen by: " + inputQuiz.playerName);//Test.
                multiPlayerRequest = true;
                return null;
            }else {                                                       //Andra spelarn som kopplar upp sig.
                inputQuiz.playerChoosingCategory = playerChoosingCategory;//När andra spelarn kopplar upp sig så skickas
                outputQuiz = inputQuiz;                                  //ett tomt Quiz till första, som väljer kategori.
                multiPlayerRequest = false;
                state = SENDINGQUIZ;
            }

        }else if (state == SENDINGQUIZ) {

             if (!multiPlayerRequest) {         //Kollar om spelarn ska välja kategori
                 if(inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory)) {
                     outputQuiz = new Quiz(inputQuiz.category);
                 }
                 multiPlayerRequest = true;
                 return null;
             }else {                            //Kollar om spelarn ska välja kategori
                 if(inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory)) {
                     outputQuiz = new Quiz(inputQuiz.category);
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

            if (!multiPlayerRequest){                   //Uppdaterar ScoreTable med en spelares svar.
                ChoosingCategory = true;
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                setPlayerChoosingCategory(inputQuiz);   //Gör så att varannan spelare väljer Kategori.
                multiPlayerRequest = true;
                return null;

            }else {

                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed); //Uppdaterar ScoreTable med en spelares svar.
                setPlayerChoosingCategory(inputQuiz);   //Gör så att varannan spelare väljer Kategori.
                System.out.println("Player Choosing Category: " + playerChoosingCategory);//Test.sout
                inputQuiz.playerChoosingCategory = playerChoosingCategory;  //Efter att båda spelare har kopplat uppsig
                inputQuiz.scoreTable = protcolScoreTable;                   //så vet man vem som väljer kategori.
                inputQuiz.readOnly = true;
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
            while (true){} //TODO Locks program to simulate end of game, changed later.
        }

        return outputQuiz;
    }

    private void setPlayerChoosingCategory(Quiz inputQuiz) {
        if((!inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
            ChoosingCategory = false;
            playerChoosingCategory = inputQuiz.playerName;
        }
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
