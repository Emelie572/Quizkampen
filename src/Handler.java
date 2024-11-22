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
            multiplayer.sendProtocalToPlayer();

            Object response;
            while ((response = in.readObject()) != null) {

                if (response instanceof Quiz) {
                    Quiz playerQuiz = (Quiz) response;

                    multiplayer.sendProtocalToPlayer(playerQuiz);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fel vid hantering av spelare: " + e.getMessage());
            e.printStackTrace();
        }
    }
}