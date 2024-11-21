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
/*
           //Quiz quiz = new Quiz();
            multiplayer.sendProtocalToPlayer();

             */
            Object response;

            while ((response = in.readObject()) != null) {

                if (response instanceof Quiz) {
                    Quiz playerQuiz = (Quiz) response;

                    multiplayer.sendProtocalToPlayer(playerQuiz);

                    //out.writeObject(playerQuiz);
                    //out.flush();

                } else if (response instanceof String) {
                    String responsename = (String) response;
                    multiplayer.playerList(responsename);
                    multiplayer.sendProtocalToPlayer();



                    //if-sats för om spelet är klart?
                    //break;

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