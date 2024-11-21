import java.io.*;
import java.net.Socket;

public class Handler extends Thread {

    private final Socket playerSocket;
    private final MultiPlayer multiplayer;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Protocol protocol;


    public Handler(Socket playerSocket, MultiPlayer multiplayer) {
        this.playerSocket = playerSocket;
        this.multiplayer = multiplayer;
    }

    @Override
    public void run() {


        try {

            out = new ObjectOutputStream(playerSocket.getOutputStream());
            in = new ObjectInputStream(playerSocket.getInputStream());
            out.flush();

            multiplayer.addPlayers(out);

           //Quiz quiz = new Quiz();
            multiplayer.sendProtocalToPlayer(null);

            while (in.readObject() != null) {
                Object response = in.readObject();
                if (response instanceof Quiz) {
                    Quiz playerQuiz = (Quiz) response;

                    multiplayer.sendProtocalToPlayer(playerQuiz);

                    if (playerQuiz != null) {
                        out.writeObject(playerQuiz);
                        out.flush();
                    }

                } else if (response == null) {
                    System.out.println("Inget svarsalternativ valdes");



                    //if-sats för om spelet är klart?
                    break;

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fel vid hantering av spelare: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}