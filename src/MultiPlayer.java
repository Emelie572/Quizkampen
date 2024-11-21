import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MultiPlayer {

    private final List<ObjectOutputStream> objectStreams = new ArrayList<>();
    private final Protocol protocol = new Protocol();
    private int playerSyncer = 0;


    public synchronized void addPlayers(ObjectOutputStream stream) {
        objectStreams.add(stream);
        //notifyAll();
    }
    public  synchronized void playerSyncer() {
        playerSyncer++;
        if(playerSyncer == 2) {
            notify();
        }
    }

    public synchronized void sendProtocalToPlayer(Quiz quiz) {
        if (playerSyncer <2) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Quiz protocolQuiz = protocol.proccesQuizInput(quiz);
        for (ObjectOutputStream stream : objectStreams) {
            try {
                stream.writeObject(protocolQuiz);
                stream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fel uppstod med quizzet: " + e.getMessage());
            }
        }
    }



    public synchronized void sendProtocalToPlayer() {
        if (playerSyncer <2) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        protocol.proccesQuizInput(null);
        }
    }

    public void syncerReset() {
        playerSyncer = 0;
    }
}

