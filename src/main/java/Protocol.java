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

    private final List<TriviaCategory> triviaCategory;
    private final int MAXROUNDS;
    private final ScoreTable protcolScoreTable;
    private int roundsPlayed = 0;
    private boolean multiPlayerRequest = false;
    private Quiz outputQuiz;
    private String playerChoosingCategory;
    private boolean ChoosingCategory = true;

    //TODO Rensa upp kod.
    public Protocol() {

        CategorySourceReader categorySourceReader = new CategorySourceReader();
        this.triviaCategory = categorySourceReader.getCategorySource().getTrivia_categories();
        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
    }

    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {

        if (state == CONNECTION) {//Connection Quiz kommer in.
            /*
            Första spelarn som kopplar upp sig
            och markeras för att välja kategori
             */
            if (!multiPlayerRequest) {
                playerChoosingCategory = inputQuiz.getPlayerName();
                System.out.println("First Category gets chosen by: " + inputQuiz.getPlayerName());//Test.
                multiPlayerRequest = true;
                return null;
                /*
                Andra spelarn som kopplar upp sig.
                När andra spelarn kopplar upp sig så skickas
                ett tomt Quiz till första, som väljer kategori.
                 */
            }else {
                inputQuiz.setPlayerChoosingCategory(playerChoosingCategory);
                inputQuiz.setTriviaCategories(triviaCategory);
                outputQuiz = inputQuiz;
                multiPlayerRequest = false;
                state = SENDINGQUIZ;
            }

        }else if (state == SENDINGQUIZ) {
             if (!multiPlayerRequest) {
                 //Kollar om spelarn ska välja kategori
                 if(inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory)) {
                     outputQuiz = new Quiz(inputQuiz.getCategory(),triviaCategory);
                     setCategoryUsed(inputQuiz.getCategory());
                 }
                 multiPlayerRequest = true;
                 return null;
             }else {
                 //Kollar om spelarn ska välja kategori
                 if(inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory)) {
                     outputQuiz = new Quiz(inputQuiz.getCategory(),triviaCategory);
                     setCategoryUsed(inputQuiz.getCategory());
                 }
                 try {
                     Thread.sleep(1000); //TODO "overload". sätt till 5000.
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multiPlayerRequest = false;
                state = ROUNDSCORING;
                System.out.println("Sending New Quiz requested by: " + inputQuiz.getPlayerName());//Test.
            }


        } else if (state == ROUNDSCORING) {
            //Uppdaterar ScoreTable med en spelares svar.
            if (!multiPlayerRequest){
                ChoosingCategory = true;
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.getPlayerName(),inputQuiz.getCorrectAnswers(),roundsPlayed);
                //Gör så att varannan spelare väljer Kategori.
                setPlayerChoosingCategory(inputQuiz);
                multiPlayerRequest = true;
                System.out.println("Updating Score from player completing it first: " + inputQuiz.getPlayerName());//Test.
                /*Tabort?
                if((!inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
                    ChoosingCategory = false;
                    playerChoosingCategory = inputQuiz.getPlayerName();
                }

                 */
                return null;

            }else {
                //Uppdaterar ScoreTable med en spelares svar.
                protcolScoreTable.updateScore(inputQuiz.getPlayerName(),inputQuiz.getCorrectAnswers(),roundsPlayed);
                //Gör så att varannan spelare väljer Kategori.
                setPlayerChoosingCategory(inputQuiz);
                System.out.println("Player Choosing Category: " + playerChoosingCategory);//Test.sout
                //Efter att båda spelare har kopplat upp sig så vet man vem som väljer kategori.
                inputQuiz.setPlayerChoosingCategory(playerChoosingCategory);
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

    private void setCategoryUsed (int id){
        for(TriviaCategory trivia: triviaCategory) {
            if(trivia.getId() == id) {
                trivia.setUsed(true);
            }
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
