import javax.swing.*;
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
       name = JOptionPane.showInputDialog(null,"Ange namn:");

        new Thread(() -> {
            try (Socket socket = new Socket(ip, port);)
            {
                input = new BufferedReader(new InputStreamReader(System.in));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                Object inputLine;
                String inputAnswer;


                //out.writeObject(name);

                while (true){
                        while ((inputLine = in.readObject()) != null)
                        {
                            if (inputLine instanceof Quiz)
                            {
                                Quiz inputQuiz = (Quiz) inputLine;

                                if(!inputQuiz.readOnly) {

                                    List<String> question = null;

                                    for (int i = 0; i < inputQuiz.allQuestions.size(); i++) {
                                        question = inputQuiz.allQuestions.get(i);

                                        printQuestion(question);

                                        while ((inputAnswer = input.readLine()) != null) {
                                            inputQuiz.correctAnswers = checkAnswer(inputAnswer, question);
                                            break;
                                        }
                                    }

                                    inputQuiz.playerName = name;
                                    question.add(String.valueOf(scorePerRound));
                                    scorePerRound = 0;
                                    out.writeObject(inputQuiz);
                                    break;
                                } else {
                                    String stringScore = String.valueOf(inputQuiz.scoreTable.getMapScores());
                                    System.out.println(stringScore);
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
