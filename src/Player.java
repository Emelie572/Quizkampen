import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Player
{
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;
    private int scorePerRound = 0;
    private String name;
    private List<Object> send;

    Player() throws UnknownHostException
    {
        new Thread(() -> {
            try (Socket socket = new Socket(ip, port);)
            {

                input = new BufferedReader(new InputStreamReader(System.in));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                Object inputLine;
                String inputAnswer;

                System.out.println("Namn? ");

                name = input.readLine();

                /* ev en while för antalet rundor som ska spelas

                läser in kategorierna och skickar tillbaka ett svar
                while((inputLine = in.readObject()) != null){
                    System.out.println(inputLine.toString());

                    while ((inputAnswer = input.readLine()) != null){
                        out.writeObject(inputAnswer);
                    }
                }*/

                while (true)
                {
                    while ((inputLine = in.readObject()) != null)
                    {
                        if (inputLine instanceof List)
                        {
                            List<Object> list = (List<Object>) inputLine;

                            if (list.get(1) instanceof Boolean && list.get(1).equals(false))
                            {
                                List<List> l = (List<List>) list.get(0);
                                for (int i = 0; i < l.size(); i++)
                                {
                                    printQuestion(l.get(i));
                                    while ((inputAnswer = input.readLine()) != null)
                                    {
                                        scorePerRound = checkAnswer(inputAnswer, l.get(i));
                                        System.out.println("Dina poäng: " + scorePerRound);
                                        break;
                                    }
                                }

                                send = new ArrayList<>();
                                send.add(name);
                                send.add(scorePerRound);
                                out.writeObject(send);
                                break;
                            }else{
                                System.out.println(inputLine);
                                break;
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                try{
                    if (out != null) out.close();
                    if (in != null) in.close();
                    if (input != null) input.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int checkAnswer(String input, List<String> correct) throws IOException
    {
        if (input.equals(correct.get(correct.size() - 1)))
        {
            System.out.println("rätt");
            scorePerRound++;
        } else
        {
            System.out.println("fel");
        }
        return scorePerRound;
    }

    public void printQuestion(List<List> question) throws IOException
    {
        for (int i = 0; i < question.size()-1; i++){
            System.out.print(question.get(i) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }
}
