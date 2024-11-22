import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreTable implements Serializable {

    private ConcurrentHashMap<String,Integer> mapScores;
    double index;

    public ScoreTable() {
    //TODO Listan är inte sorterad.
        mapScores = new ConcurrentHashMap<>();

        this.index = Math.random();
        //int intindex  = (int )(index*10000);
        //updateScoreTable("ID",intindex);


    }
    //TODO Problem om två spelare har samma namn.
    public void updateScoreTable(String playerName, int score) {

        if (mapScores.containsKey(playerName)) {
            mapScores.put(playerName, (mapScores.get(playerName) + score));
        } else{
            mapScores.put(playerName, score);
        }
    }

    public ConcurrentHashMap<String, Integer> getMapScores() {
        return mapScores;
    }

    public void setMapScores(ConcurrentHashMap<String, Integer> mapScores) {
        this.mapScores = mapScores;
    }
}
