import Database.CategorySourceReader;
import Database.TriviaCategory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Protocol {

    private final int CONNECTION = 0;
    private final int SENDINGQUIZ = 1;
    private final int ROUNDSCORING = 2;
    private final int ENDOFGAME = 3;
    private final int STOPGAME = 4;
    private int state = CONNECTION;


    private final List<String> players = new ArrayList<>();
    private final List<TriviaCategory> triviaCategory;
    private final int MAXROUNDS;
    private final ScoreTable protcolScoreTable;
    private int roundsPlayed = 0;
    private boolean multiPlayerRequest = false;
    private Quiz outputQuiz;
    private String playerChoosingCategory;
    private boolean ChoosingCategory = true;

    public Protocol() {

        CategorySourceReader categorySourceReader = new CategorySourceReader();
        this.triviaCategory = categorySourceReader.getCategorySource().getTrivia_categories();
        this.MAXROUNDS = getRoundProperty();
        this.protcolScoreTable = new ScoreTable(MAXROUNDS);
    }

    public synchronized Quiz processQuizInput(Quiz inputQuiz)  {

        if (state == CONNECTION) {
            if (!multiPlayerRequest) {
                players.add(inputQuiz.getPlayerName());
                playerChoosingCategory = inputQuiz.getPlayerName();
                multiPlayerRequest = true;
                return null;
            }else {
                players.add(inputQuiz.getPlayerName());
                inputQuiz.setPlayerChoosingCategory(playerChoosingCategory);
                inputQuiz.setTriviaCategories(triviaCategory);
                inputQuiz.setPlayersInGame(players);
                outputQuiz = inputQuiz;
                multiPlayerRequest = false;
                state = SENDINGQUIZ;
            }

        }else if (state == SENDINGQUIZ) {
             if (!multiPlayerRequest) {
                 if(inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory)) {
                     inputQuiz.setCategory(StringToInt(inputQuiz.getCategoryString()));
                     System.out.println(inputQuiz.getCategory());

                     setCategoryUsed(inputQuiz.getCategory());
                     outputQuiz = new Quiz(inputQuiz.getCategory(),triviaCategory);
                 }
                 multiPlayerRequest = true;
                 return null;
             }else {
                 if(inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory)) {
                     inputQuiz.setCategory(StringToInt(inputQuiz.getCategoryString()));
                     System.out.println(inputQuiz.getCategory());

                     setCategoryUsed(inputQuiz.getCategory());
                     outputQuiz = new Quiz(inputQuiz.getCategory(),triviaCategory);
                 }/*
                 try {
                     Thread.sleep(5000); //TODO "Overload" om det är snabbare än 5sec mellan request till URL-server.
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                multiPlayerRequest = false;
                state = ROUNDSCORING;
            }

        } else if (state == ROUNDSCORING) {
            if (!multiPlayerRequest){
                ChoosingCategory = true;
                roundsPlayed++;
                protcolScoreTable.updateScore(inputQuiz.getPlayerName(),inputQuiz.getCorrectAnswers(),roundsPlayed);
                setPlayerChoosingCategory(inputQuiz);
                multiPlayerRequest = true;
                return null;

            }else {
                protcolScoreTable.updateScore(inputQuiz.getPlayerName(),inputQuiz.getCorrectAnswers(),roundsPlayed);
                setPlayerChoosingCategory(inputQuiz);
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
            outputQuiz = new Quiz();
            state =STOPGAME;
        }

        else if(state == STOPGAME){
            while (true);
        }

        return outputQuiz;
    }

    private void setPlayerChoosingCategory(Quiz inputQuiz) {
        if((!inputQuiz.getPlayerName().equalsIgnoreCase(playerChoosingCategory))&& ChoosingCategory){
            ChoosingCategory = false;
            playerChoosingCategory = inputQuiz.getPlayerName();
            System.out.println("setPlayerChoosingCategory: " + playerChoosingCategory);
        }
    }

    private void setCategoryUsed (int id){
        for(TriviaCategory trivia: triviaCategory) {
            if(trivia.getId() == id) {
                trivia.setUsed(true);
                System.out.println("setCategoryUsed: " + trivia.getName());
            }
        }
    }

    private int StringToInt(String input) {
        for(TriviaCategory trivia: triviaCategory) {
            if(trivia.getName().equalsIgnoreCase(input)) {
                return trivia.getId();
            }
        }
        return 0;
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
