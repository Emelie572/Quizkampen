import java.io.*;
import java.net.Socket;

public class Handler implements Runnable {

    private final Socket playerSocket;
    private final MultiPlayer multiplayer;
    // private String playerName;

    public Handler(Socket playerSocket, MultiPlayer multiplayer) {
        this.playerSocket = playerSocket;
        this.multiplayer = multiplayer;
    }

    @Override
    public void run() {


        try (ObjectOutputStream out = new ObjectOutputStream(playerSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(playerSocket.getInputStream())) {

            //

            try (PrintWriter outStream = new PrintWriter(this.playerSocket.getOutputStream(), true);
                 BufferedReader inStream = new BufferedReader(new InputStreamReader(this.playerSocket.getInputStream()))) {

                multiplayer.addPlayer(outStream);

                multiplayer.waitForTwoPlayers(outStream);


            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}