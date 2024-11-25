import Database.QuestionsQuiz;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Player
{
    //Development
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;
    private String name;

    List<QuestionsQuiz> questionsQuiz = new ArrayList<>();

    Player() throws UnknownHostException
    {
        this.name = JOptionPane.showInputDialog(null,"Ange namn:");
        System.out.println(name);
        name += "_"+Math.random(); //Unikt namn för HashMaps.

            try (Socket socket = new Socket(ip, port);)
            {
                input = new BufferedReader(new InputStreamReader(System.in));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

              Quiz connectionQuiz = new Quiz(questionsQuiz);
              out.writeObject(connectionQuiz);

                Object inputLine;

                while (true){
                        while ((inputLine = in.readObject()) != null)
                        {
                            if (inputLine instanceof Quiz )
                            {
                                Quiz inputQuiz = (Quiz)inputLine;

                                if(!inputQuiz.readOnly) {

                                    //List<String> question;
                                    List<QuestionsQuiz> questionList = inputQuiz.getQuestions();
                                        printQuestion(questionList);

                                    String inputAnswer = input.readLine().trim();
                                    inputQuiz.correctAnswers += checkAnswer(inputAnswer, questionList);

                                    /*
                                    for (int i = 0; i < inputQuiz.allQuestions.size(); i++) {

                                        question = inputQuiz.allQuestions.get(i);
                                        printQuestion(question);


                                        String inputAnswer = input.readLine().trim();
                                        inputQuiz.correctAnswers += checkAnswer(inputAnswer, question);
                                    }

                                     */
                                    inputQuiz.playerName = name;

                                } else {
                                    System.out.println(inputQuiz.scoreTable.toString());
                                }
                                out.writeObject(inputQuiz);
                                break;
                            }
                        }
                }
            } catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
    }

    public int checkAnswer(String input, List<String> question) throws IOException
    {
        int correctAnswer = Integer.parseInt(question.getLast());
        String correct = question.get(correctAnswer).trim();

        if (input.equalsIgnoreCase(correct))
        {
            System.out.println("Rätt!\n");
            return 1;

        }else {System.out.println("Fel!\n");}

        return 0;
    }

    public void printQuestion(List<QuestionsQuiz> questionList) {
        for (QuestionsQuiz questions : questionList) {
            System.out.println(questions);
        }
    }

    /*
    public void printQuestion(List<String> question) throws IOException
    {
        for (int i = 0; i < question.size()-1; i++){
            System.out.print(question.get(i) + " ");
        }
        System.out.println();
    }


     */
    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }
}
