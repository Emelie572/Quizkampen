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
                out.writeObject(name);


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
                            if (inputLine instanceof Quiz)
                            {
                                Quiz inputQuiz = (Quiz) inputLine;

                                List<String> question = null;

                                for (int i = 0; i < inputQuiz.allQuestions.size(); i++)
                                {
                                    question =  inputQuiz.allQuestions.get(i);

                                    printQuestion(question);

                                    while ((inputAnswer = input.readLine()) != null)
                                    {
                                        inputQuiz.correctAnswers = checkAnswer(inputAnswer, question);
                                        break;
                                    }
                                }

                                inputQuiz.playerName = name;
                                question.add(String.valueOf(scorePerRound));
                                scorePerRound = 0;
                                //inputLine = allQuestions;
                                out.writeObject(inputQuiz);
                                break;
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

    public int checkAnswer(String input, List<String> question) throws IOException
    {
        int correctAnswer = Integer.parseInt(question.getLast());
        if (input.equals(question.get(correctAnswer)))
        {
            System.out.println("rätt");
            scorePerRound++;
        }
        return scorePerRound;
    }

    public void printQuestion(List<String> question) throws IOException
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
