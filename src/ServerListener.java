import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    private int port = 12345;

    ServerListener() throws IOException
    {
        MultiPlayer multiPlayer = new MultiPlayer();
        while(true)
        {
            try(ServerSocket serverSocket = new ServerSocket(port);)
            {
                Socket socket = serverSocket.accept();
                Handler playerHandler = new Handler(socket, multiPlayer);
                playerHandler.start();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        new ServerListener();
    }
}
