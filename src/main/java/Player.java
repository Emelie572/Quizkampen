import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

                //int testCategory = 10;//Test. Kategori input, Books
                int testCategory = randomCategory(); //Test. Slumpar fram en kategori
                Quiz connectionQuiz = new Quiz(testCategory,true);
                connectionQuiz.playerName = name;//Test. Sätter namn på quiz.
                out.writeObject(connectionQuiz);

                Object inputLine;

                while (true){
                        while ((inputLine = in.readObject()) != null)
                        {
                            if (inputLine instanceof Quiz )
                            {
                                Quiz inputQuiz = (Quiz)inputLine;

                                if(!inputQuiz.readOnly) {

                                    List<String> question;

                                    for (int i = 0; i < inputQuiz.allQuestions.size(); i++) {

                                        question = inputQuiz.allQuestions.get(i);
                                        printQuestion(question);
                                        String inputAnswer = input.readLine().trim();
                                        inputQuiz.correctAnswers += checkAnswer(inputAnswer, question);
                                    }
                                    inputQuiz.playerName = name;

                                } else { //readOnly
                                    System.out.println(inputQuiz.scoreTable.toString());
                                    System.out.println("Player choosing category: " + inputQuiz.playerChoosingCategory);
                                    inputQuiz.setCategory(randomCategory());//Test. Kategori ska sättas.
                                    inputQuiz.playerName = name;//Test.
                                }
                                System.out.println("PlayerName printer"+inputQuiz.playerName+" "+inputQuiz.category);
                                out.writeObject(inputQuiz); //Test. Efter readOnly skickas båda samtidigt och player 1 går alltid först.
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

    public void printQuestion(List<String> question) throws IOException
    {
        for (int i = 0; i < question.size()-1; i++){
            String rewrite = question.get(i).replaceAll("&#039;","'");
            rewrite = rewrite.replaceAll("&quot;","\"");
            rewrite = rewrite.replaceAll("&amp;","&");
            System.out.print(rewrite + ", ");
        }
        System.out.println();
    }

    private int randomCategory(){ //Test. Metod för att slumpa fram en kategori
        int randomCategory = (int)(Math.random()*(32 - 9 + 1)) + 9;
        System.out.println(randomCategory);
        return randomCategory;
    }

    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }
}
