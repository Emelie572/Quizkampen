import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Player {
    //Development
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;
    private String name;

    Player() throws UnknownHostException
    {
        this.name = JOptionPane.showInputDialog(null,"Ange namn:"); //TODO Panel för namn. Ersätts med GUI.
        System.out.println(name); //TODO Ersätts med GUI.
        name += "_"+Math.random(); //Unikt namn för HashMaps.

            try (Socket socket = new Socket(ip, port))
            {
                input = new BufferedReader(new InputStreamReader(System.in));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                Quiz connectionQuiz = new Quiz(name);
                out.writeObject(connectionQuiz);

                Object inputLine;

                while (true){
                        while ((inputLine = in.readObject()) != null) {

                            if (inputLine instanceof Quiz ) {
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
                                    if(inputQuiz.scoreTable!=null) {
                                        System.out.println(inputQuiz.scoreTable); //TODO ScoreTable Skrivs ut. Ersätts med GUI.
                                    }

                                    inputQuiz.playerName = name;
                                    if(name.equalsIgnoreCase(inputQuiz.playerChoosingCategory)) {
                                        //inputQuiz.setCategory(randomCategory());//TODO Kategori slumpas fram. Ersätts med GUI.
                                        inputQuiz.setCategory(Integer.parseInt(JOptionPane.showInputDialog(name+" is Choosing Category.")));
                                    }
                                }
                                out.writeObject(inputQuiz); //Efter readOnly skickas båda samtidigt och player 1 går först i strömmen.
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
            System.out.println("Rätt!\n");//TODO Ersätts i GUI.
            return 1;

        }else {System.out.println("Fel!\n");}//TODO Ersätts i GUI.

        return 0;
    }

    public void printQuestion(List<String> question) throws IOException //TODO Ersätts med GUI.
    {
        for (int i = 0; i < question.size()-1; i++){
            String rewrite = question.get(i).replaceAll("&#039;","'");
            rewrite = rewrite.replaceAll("&quot;","\"");
            rewrite = rewrite.replaceAll("&amp;","&");
            rewrite = rewrite.replaceAll("&Delta;","Δ");
            rewrite = rewrite.replaceAll("&Uuml;","Ü");
            rewrite = rewrite.replaceAll("&ouml;","ö");
            System.out.print(rewrite + ", ");
        }
        System.out.println();
    }

    private int randomCategory(){ //TODO Test. Metod för att slumpa fram en kategori. Ta bort.
        return (int)(Math.random()*(32 - 9 + 1)) + 9;
    }

    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }
}
