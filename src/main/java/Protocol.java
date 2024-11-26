
import Database.CategorySourceReader;
import Database.TriviaCategory;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

public class Protocol {

    private final int CONNECTION = 0;
    private final int SENDINGQUIZ = 1;
    private final int ROUNDSCORING = 2;
    private final int ENDOFGAME = 3;
    private int state = CONNECTION;

    private List<TriviaCategory> triviaCategory;
    private final int MAXROUNDS;
    private final ScoreTable protcolScoreTable;
    private int roundsPlayed = 0;
    private boolean multiPlayerRequest = false;
    private Quiz outputQuiz;
    private int chosenCategory;
    private String playerChoosingCategory;
    private boolean ChoosingCategory = true;

    //TODO Rensa upp kod.
    public Protocol() {

        CategorySourceReader categorySourceReader = new CategorySourceReader();
        this.triviaCategory = categorySourceReader.getCategorySource().getTrivia_categories();
        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
        //this.quizSource = new QuizSourceReader(0,0).getQuizSource();
    }

    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {

        if (state == CONNECTION) {//Connection Quiz kommer in.

            if (!multiPlayerRequest) {                          //Första spelarn som kopplar upp sig.
                playerChoosingCategory = inputQuiz.getPlayerName();  //och markeras för att välja kategori
                System.out.println("First Category gets chosen by: " + inputQuiz.getPlayerName());//Test.
                multiPlayerRequest = true;
                return null;
            }else {                                                       //Andra spelarn som kopplar upp sig.
                inputQuiz.setPlayerChoosingCategory(playerChoosingCategory);//När andra spelarn kopplar upp sig så skickas
                outputQuiz = inputQuiz;                                  //ett tomt Quiz till första, som väljer kategori.
                multiPlayerRequest = false;
                state = SENDINGQUIZ;

            }

        }else if (state == SENDINGQUIZ) {

             if (!multiPlayerRequest) {         //Kollar om spelarn ska välja kategori
                 if(inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory)) {
                     outputQuiz = new Quiz(inputQuiz.getCategory());
                 }
                 multiPlayerRequest = true;
                 return null;
             }else {                            //Kollar om spelarn ska välja kategori
                 if(inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory)) {
                     outputQuiz = new Quiz(inputQuiz.getCategory());
                 }
                 try {
                     Thread.sleep(1000); //TODO "overload". sätt till 5000.
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //playerChoosingCategory = inputQuiz.playerName;
                multiPlayerRequest = false;
                state = ROUNDSCORING;
                System.out.println("Sending New Quiz requested by: " + inputQuiz.getPlayerName());//Test.
            }


        } else if (state == ROUNDSCORING) {

            if (!multiPlayerRequest){                   //Uppdaterar ScoreTable med en spelares svar.
                ChoosingCategory = true;
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.getPlayerName(),inputQuiz.getCorrectAnswers(),roundsPlayed);
                setPlayerChoosingCategory(inputQuiz);   //Gör så att varannan spelare väljer Kategori.
                multiPlayerRequest = true;
                System.out.println("Updating Score from player completing it first: " + inputQuiz.getPlayerName());//Test.
                if((!inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
                    ChoosingCategory = false;
                    playerChoosingCategory = inputQuiz.getPlayerName();
                }
                return null;

            }else {

                protcolScoreTable.updateScore(inputQuiz.getPlayerName(),inputQuiz.getCorrectAnswers(),roundsPlayed); //Uppdaterar ScoreTable med en spelares svar.
                setPlayerChoosingCategory(inputQuiz);   //Gör så att varannan spelare väljer Kategori.
                System.out.println("Player Choosing Category: " + playerChoosingCategory);//Test.sout
                inputQuiz.setPlayerChoosingCategory(playerChoosingCategory); //Efter att båda spelare har kopplat uppsig //så vet man vem som väljer kategori.
                inputQuiz.setScoreTable(protcolScoreTable);
                inputQuiz.setReadOnly(true);
                outputQuiz = inputQuiz;
                multiPlayerRequest = false;

                if (MAXROUNDS == roundsPlayed){
                    state = ENDOFGAME;
                    outputQuiz.setPlayerChoosingCategory(null);
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
        if((!inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
            ChoosingCategory = false;
            playerChoosingCategory = inputQuiz.getPlayerName();
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
