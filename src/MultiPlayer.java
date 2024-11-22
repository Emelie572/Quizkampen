import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MultiPlayer {

    private final List<ObjectOutputStream> objectStreams = new ArrayList<>();
    private final Protocol protocol = new Protocol();

    public synchronized void addPlayers(ObjectOutputStream stream) {

        objectStreams.add(stream);
    }

    public synchronized void sendProtocalToPlayer(Quiz quiz) {

        Quiz protocolQuiz = protocol.proccesQuizInput(quiz);
        for (ObjectOutputStream stream : objectStreams) {
            try {
                if(protocolQuiz!=null) {
                    stream.writeObject(protocolQuiz);
                    stream.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fel uppstod med quizzet: " + e.getMessage());
            }
        }
    }
}

