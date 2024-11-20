
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<ListOfServers> players = new ArrayList<>();
    private ServerListener serverListener;

    public Server(int port) {
        serverListener = new ServerListener(port);
    }

    public void start() {
        serverListener.start(this);
    }

    public void addPlayer(Socket playerSocket) {
        ListOfServers listOfServers = new ListOfServers(playerSocket);
        players.add(listOfServers);
        new Thread(listOfServers).start();
    }

    public static void main (String[] args) {
        Server server = new Server(12345);
        server.start();


    }
}
