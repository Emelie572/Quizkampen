import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    //Development
    private int port = 12345;


    ServerListener() throws IOException, InterruptedException {
        MultiPlayer multiPlayer = new MultiPlayer();
        while(true)
        {
            try(ServerSocket serverSocket = new ServerSocket(port);) {
                Socket socket = serverSocket.accept();
                if (multiPlayer.getObjectStreams().size() < 2) {
                    Handler playerHandler = new Handler(socket, multiPlayer);
                    playerHandler.start();
                } else {
                    multiPlayer = new MultiPlayer();
                    Handler playerHandler = new Handler(socket, multiPlayer);
                    playerHandler.start();
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new ServerListener();
    }
}