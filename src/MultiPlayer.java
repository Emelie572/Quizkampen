import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Handler;

public class MultiPlayer {

    private final List<PrintWriter> writers = new ArrayList<>();
    private final List<String> players = new ArrayList<>();
    private Protocol protocol;
    //private  Quiz quiz;
    //private boolean gameStarted = false;
    //MultiPlayer multiPlayer = new MultiPlayer();


    public synchronized void addPlayer(PrintWriter writer) {
        writers.add(writer);
        notifyAll();
    }

    public synchronized void waitForTwoPlayers(PrintWriter outstream) throws InterruptedException {
        if (writers.size() == 1) {
            outstream.println("Väntar på två spelare");

            while (writers.size() > 2) {
                wait();
            }

            for (PrintWriter writer : writers) {
                writer.println("Två spelare är anslutna! Nu börjar spelet");
            }


            public synchronized void sendQuizPlayerOne (Quiz quiz){
                if (writers.size() == 2) { //
                    PrintWriter firstPlayer = writers.get(0);
                    PrintWriter secondPlayer = writers.get(1);

                    firstPlayer.println(quiz.toString());

                    secondPlayer.println("Spelare ett spelar");
                }


                public synchronized void sendQuizPlayerTwo (Quiz quiz){
                    if (writers.size() == 2) {
                        PrintWriter secondPlayer = writers.get(1);

                        secondPlayer.println(quiz.toString());
                    }
                }
            }
        }

    }
}