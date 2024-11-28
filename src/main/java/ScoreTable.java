import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreTable implements Serializable {

    private final List<ConcurrentHashMap<String,Integer>> gameScore = new ArrayList<>();

    public ScoreTable(int rounds) {
        for (int i = 0; i <= rounds; i++) {
            gameScore.add(new ConcurrentHashMap<>());
        }
    }
    public void updateScore(String playerName, int score, int round) {

        if(gameScore.getFirst().containsKey(playerName)){
            gameScore.getFirst().put(playerName, gameScore.getFirst().get(playerName) + score);
        } else{
            gameScore.getFirst().put(playerName, score);
        }
        gameScore.get(round).put(playerName, score);
    }

    @Override
    public String toString() {

        StringBuilder returnString= new StringBuilder();
        String gameScoreString;

        for (int i = 0; i <gameScore.size() ; i++) {

                if (i == 0) {
                    gameScoreString = stringRefactoring(gameScore.get(i).toString());
                    returnString = new StringBuilder("Total  : " + gameScoreString + "\n");

                } else {
                    if(!gameScore.get(i).isEmpty()) {

                        gameScoreString = stringRefactoring(gameScore.get(i).toString());
                        returnString.append("Round ").append(i).append(": ").append(gameScoreString).append("\n");
                    }else {
                        returnString.append("Round ").append(i).append(": ").append("\n");
                    }
                }
        }
        return returnString.toString();
    }

    private String stringRefactoring(String s){

        String player1 = s.substring(1,s.indexOf("_"));
        String player1Score = s.substring(s.indexOf("=")+1, s.indexOf(","));
        String player2 = s.substring(s.indexOf(","), s.lastIndexOf("_"));
        String player2Score = String.valueOf(s.charAt(s.length()-2));
        return player1 +" "+player1Score+" "+player2+" "+player2Score;
    }

    public List<ConcurrentHashMap<String, Integer>> getGameScore() {
        return gameScore;
    }
}
