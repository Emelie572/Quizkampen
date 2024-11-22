
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreTable implements Serializable {

    ConcurrentHashMap<String,Integer> mapScores;

    public ScoreTable() {
    //TODO Listan är inte sorterad.
        this.mapScores = new ConcurrentHashMap<>();
    }

    //TODO Problem om två spelare har samma namn.
    public void updateScoreTable(String playerName, int score) {

        if (mapScores.containsKey(playerName)) {
            mapScores.put(playerName, (mapScores.get(playerName) + score));
            System.out.println(mapScores + " ScoreTable Print");
        } else{
            mapScores.put(playerName, score);
        }

    }

    public ConcurrentHashMap<String, Integer> getMapScores() {
        return mapScores;
    }
}
