import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MultiPlayer {

    private final List<ObjectOutputStream> objectStreams = new ArrayList<>();
    private final Protocol protocol = new Protocol();
    private List<String> players = new ArrayList<>();


    public synchronized void addPlayers(ObjectOutputStream stream) {
        objectStreams.add(stream);
        //notifyAll();
    }

    public synchronized void sendProtocalToPlayer(Quiz quiz) {
        while (players.size() <2) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (ObjectOutputStream stream : objectStreams) {
            try {
                stream.writeObject(protocol.proccesQuizInput(quiz));
                stream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fel uppstod med quizzet: " + e.getMessage());
            }
        }
    }

    public  synchronized void  playerList(String name) {
        players.add(name);
        if(players.size() == 2) {
            notify();
        }
    }

    public synchronized void sendProtocalToPlayer() {
        while (players.size() <2) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (ObjectOutputStream stream : objectStreams) {
            try {
                stream.writeObject(protocol.proccesQuizInput(null));
                stream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fel uppstod med quizzet: " + e.getMessage());
            }
        }
    }
}

