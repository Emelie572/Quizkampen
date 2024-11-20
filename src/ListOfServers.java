import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ListOfServers implements Runnable {

    private Socket playerSocket;


    public ListOfServers(Socket socket) {
        this.playerSocket = socket;
    }

    @Override
    public void run() {

        try (PrintWriter outStream = new PrintWriter(this.playerSocket.getOutputStream(), true);
             BufferedReader inStream = new BufferedReader(new InputStreamReader(this.playerSocket.getInputStream()))) {

             String outputLine;
             String inputLine;

            Protocol quizProtocol = new Protocol();
            outputLine = quizProtocol.startQuiz();
            outStream.println(outputLine);

            while ((inputLine = inStream.readLine()) != null) {
                outputLine = quizProtocol.playerAnswers(inputLine);
                outStream.println(outputLine);

                if (quizProtocol.quitGame()) {
                break;
            }
        }
    } catch (IOException e ) {
            System.out.println("Error med anslutning " + e.getMessage());

        }
    }
}
