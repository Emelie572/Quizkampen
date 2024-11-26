import java.io.FileInputStream;
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
    private int chosenCategory;
    private String playerChoosingCategory;
    private boolean playerCategoryChosen = true;

    //TODO Rensa upp kod.
    public Protocol() {

        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
        //this.quizSource = new QuizSourceReader(0,0).getQuizSource();
    }
    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
             if (!multiPlayerRequest) {
                 if(roundsPlayed == 0) { //Connection Quiz.
                     chosenCategory = inputQuiz.category; //Spelare 1
                     System.out.println("Category set by: " + inputQuiz.playerName + " " + inputQuiz.category);//Test.
                 } else {//Efter runda 1
                     if(inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory)) {
                         chosenCategory = inputQuiz.category;
                         outputQuiz = new Quiz(chosenCategory);
                     }
                 }
                 multiPlayerRequest = true;
                return null;
            }else {
                 if(roundsPlayed == 0) { //Connection Quiz.
                     outputQuiz = new Quiz(chosenCategory);
                     //System.out.println("Category set by: " + inputQuiz.playerName + " " +inputQuiz.category);//Test.
                     //playerChoosingCategory = inputQuiz.playerName; //Spelare 2
                 }else {
                     //Efter rund 1
                     if(inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory)) {
                         chosenCategory = inputQuiz.category;
                         outputQuiz = new Quiz(chosenCategory);
                     }
                 }
                try {
                    Thread.sleep(1000); //Undviker request "overload". 5000
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //playerChoosingCategory = inputQuiz.playerName;
                multiPlayerRequest = false;
                state = ROUNDSCORING;
                 System.out.println("Sending New Quiz requested by: " + inputQuiz.playerName);//Test.
            }


        } else if (state == ROUNDSCORING) {
            if (!multiPlayerRequest){
                playerCategoryChosen = true;
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                multiPlayerRequest = true;
                System.out.println("Updating Score from player completing it first: " + inputQuiz.playerName);//Test.
                if((!inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory))&&playerCategoryChosen){
                    playerCategoryChosen = false;
                    playerChoosingCategory = inputQuiz.playerName;
                }
                return null;
            }else {
                protcolScoreTable.updateScore(inputQuiz.playerName,inputQuiz.correctAnswers,roundsPlayed);
                inputQuiz.scoreTable = protcolScoreTable;
                System.out.println("Updating Score from player completing it second: " + inputQuiz.playerName);//Test.
                System.out.println(inputQuiz.scoreTable);//test
                inputQuiz.readOnly = true;
                if((!inputQuiz.playerName.equalsIgnoreCase(playerChoosingCategory))&&playerCategoryChosen){
                    playerCategoryChosen = false;
                    playerChoosingCategory = inputQuiz.playerName;
                } //test
                System.out.println("Player Choosing Category: " + playerChoosingCategory);
                inputQuiz.playerChoosingCategory = playerChoosingCategory;
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
