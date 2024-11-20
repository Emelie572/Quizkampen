import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player
{
    InetAddress ip = InetAddress.getLocalHost();
    int port = 12345;
    ObjectOutputStream out;
    ObjectInputStream in;

    Player() throws UnknownHostException
    {
        new Thread(() -> {
            try (Socket socket = new Socket(ip, port);
                 BufferedReader input = new BufferedReader(new InputStreamReader(System.in));)
            {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                Object inputLine;
                Object outputLine;


                while ((inputLine = in.readObject()) != null)
                {
                    System.out.println(inputLine);
                    out.writeObject(input.readLine());
                    outputLine = in.readObject();
                    System.out.println(outputLine);
                }

            } catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }
}
