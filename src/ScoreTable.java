
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreTable implements Serializable {

    private  ConcurrentHashMap<String,Integer> mapScores;
    public List<TestRondScore> testRondScores = new ArrayList<>();
    double index;

    public ScoreTable() {
    //TODO Listan är inte sorterad.
        this.mapScores = new ConcurrentHashMap<>();
        this.index = Math.random();

    }

    //TODO Problem om två spelare har samma namn.
    public void updateScoreTable(String playerName, int score) {

        if (mapScores.containsKey(playerName)) {
            mapScores.put(playerName, (mapScores.get(playerName) + score));
            System.out.println(mapScores + " ScoreTable Print");
            System.out.println(index+" ScoreTable Print");
        } else{
            mapScores.put(playerName, score);
            System.out.println(mapScores + " ScoreTable Print");
            System.out.println(index+" ScoreTable Print");
        }
        TestRondScore testRondScore = new TestRondScore(playerName,score);
        testRondScores.add(testRondScore);


    }

    public ConcurrentHashMap<String, Integer> getMapScores() {
        return mapScores;
    }

    public List<TestRondScore> getTestRondScores() {
        return testRondScores;
    }

    public void addRound ()
}
