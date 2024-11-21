import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MultiPlayer {

    private final List<ObjectOutputStream> objectStreams = new ArrayList<>();
    private Protocol protocol;


    public synchronized void addPlayers(ObjectOutputStream stream) {
        objectStreams.add(stream);
        notifyAll();
    }

    public synchronized void sendProtocalToPlayer(Quiz quiz) {
        for (ObjectOutputStream stream : objectStreams) {
            try {
                stream.writeObject(protocol.proccesQuizInput(quiz));
                stream.flush();
            } catch (Exception e) {
                System.err.println("Fel uppstod med quizzet: " + e.getMessage());
            }
        }
    }
}

