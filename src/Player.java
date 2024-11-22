import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Player
{
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;
    private int scorePerRound;
    private String name;

    Player() throws UnknownHostException
    {
        name = JOptionPane.showInputDialog(null,"Ange namn:");
        System.out.println(name);
       // new Thread(() -> {
            try (Socket socket = new Socket(ip, port);)
            {
                input = new BufferedReader(new InputStreamReader(System.in));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                Quiz connectionQuiz = new Quiz();
                out.writeObject(connectionQuiz);

                Object inputLine;
                String inputAnswer;
                while (true){
                        while ((inputLine = in.readObject()) != null)
                        {
                            if (inputLine instanceof Quiz)
                            {
                                Quiz inputQuiz = (Quiz) inputLine;
                                System.out.println(inputQuiz.index +" quiz Index");//test
                                System.out.println(inputQuiz.scoreTable.getMapScores());
                                if(!inputQuiz.readOnly) {

                                    List<String> question;

                                    for (int i = 0; i < inputQuiz.allQuestions.size(); i++) {
                                        question = inputQuiz.allQuestions.get(i);
                                        //System.out.println(inputQuiz.index +" quiz Index");//test
                                        printQuestion(question);

                                        while ((inputAnswer = input.readLine()) != null) {
                                            inputQuiz.correctAnswers = checkAnswer(inputAnswer, question);
                                            System.out.println(inputQuiz.index +" quiz Index");//test
                                            break;
                                        }
                                    }

                                    inputQuiz.playerName = name;
                                    //inputQuiz.correctAnswers = scorePerRound;
                                    scorePerRound = 0;
                                    System.out.println(inputQuiz.index +" quiz Index");//test
                                    out.writeObject(inputQuiz);
                                    //break;test
                                } else {
                                    System.out.println(inputQuiz.scoreTable.getMapScores());
                                    System.out.println(inputQuiz.index +" quiz index");//test
                                    out.writeObject(inputQuiz);
                                    //break;test
                                }
                            }
                        }
                }
            } catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
       // }).start();
    }

    public int checkAnswer(String input, List<String> question) throws IOException
    {
        int correctAnswer = Integer.parseInt(question.getLast());
        String correct = question.get(correctAnswer);
        if (input.toLowerCase().equals(correct.toLowerCase().trim()))
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
