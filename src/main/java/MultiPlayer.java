import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MultiPlayer {
    //Development
    private final List<ObjectOutputStream> objectStreams = new ArrayList<>();
    private final Protocol protocol = new Protocol();

    public synchronized void addPlayers(ObjectOutputStream stream) {

        objectStreams.add(stream);
    }

    public synchronized void sendProtocolToPlayer(Quiz quiz) {//typo

        Quiz protocolQuiz = protocol.processQuizInput(quiz);

        for (ObjectOutputStream stream : objectStreams) {
            try {
                if(protocolQuiz!=null) {
                    stream.writeObject(protocolQuiz);
                    stream.reset();
                    stream.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fel uppstod med quizzet: " + e.getMessage());
            }
        }
    }

    public List<ObjectOutputStream> getObjectStreams() {
        return objectStreams;
    }
}

