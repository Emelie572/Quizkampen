import Database.TriviaCategory;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player extends JFrame implements ActionListener
{
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;
    private String name;

    private JPanel bottompanel = new JPanel();
    JPanel buttonSpace = new JPanel();
    JButton getToCategory = new JButton("Play");
    JButton getToQuestion = new JButton("Play");
    JButton getToScoreTable = new JButton("Play");
    int currentQuestion = 0;

    StartGUI startGUI;
    CategoryGUI categoryGUI;
    QuestionGUI questionGUI;

    Player() throws UnknownHostException
    {
        startGUI = new StartGUI();
        categoryGUI = new CategoryGUI();
        questionGUI = new QuestionGUI();

        getToCategory.addActionListener(this);
        getToQuestion.addActionListener(this);
        getToScoreTable.addActionListener(this);
        getToCategory.setPreferredSize(new Dimension(70, 50));
        getToCategory.setOpaque(true);
        getToCategory.setBorder(new LineBorder(Color.BLACK, 1, true));

        setTitle("Quizkampen");

        buttonSpace.add(getToCategory);
        buttonSpace.add(getToQuestion);
        buttonSpace.add(getToScoreTable);
        getToQuestion.setVisible(false);
        getToScoreTable.setVisible(false);
        setLayout(new BorderLayout());

        add(buttonSpace, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        bottompanel.add(startGUI);
        setSize(400, 300);
        add(bottompanel, BorderLayout.CENTER);


        //this.name = JOptionPane.showInputDialog(null, "Ange namn:");
        //System.out.println(name);
        //name += "_" + Math.random(); //Unikt namn för HashMaps.

        getToCategory.setVisible(false);

        while (startGUI.getname() == null || startGUI.getname().isEmpty()) {
            try {
                Thread.sleep(100);
                this.name = startGUI.getname();
                name += "_" + Math.random(); //Unikt namn för HashMaps.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try (Socket socket = new Socket(ip, port);)
        {
            input = new BufferedReader(new InputStreamReader(System.in));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println(name+ " hej");

            //int testCategory = 10;//Test. Kategori input, Books
            int testCategory = randomCategory(); //Test. Slumpar fram en kategori
            Quiz connectionQuiz = new Quiz(name);
            connectionQuiz.setPlayerName(name);//Test. Sätter namn på quiz.
            out.writeObject(connectionQuiz);
            Object inputLine;

            while (true)
            {
                while ((inputLine = in.readObject()) != null)
                {
                        //categoryGUI.setCategories(categoryList);
                    if (inputLine instanceof Quiz) {

                        Quiz inputQuiz = (Quiz) inputLine;

                        if (!inputQuiz.isReadOnly()) {
                            List<String> question;

                            for (int i = 0; i < inputQuiz.getAllQuestions().size(); i++)
                            {
                                question = inputQuiz.getAllQuestions().get(i);
                                printQuestion(question);
                                questionGUI.reset();
                                questionGUI.printQuestion(inputQuiz.getAllQuestions().get(i));
                                questionGUI.reset();
                                String inputAnswer = input.readLine().trim();
                                inputQuiz.addToCorrectAnswers(checkAnswer(inputAnswer, question));
                            }
                            inputQuiz.setPlayerName(name);
                        } else {//readOnly

                            if(inputQuiz.getScoreTable()!=null){
                                System.out.println(inputQuiz.getScoreTable().toString());
                            }
                            //System.out.println("Player choosing category: " + inputQuiz.getPlayerChoosingCategory());

                            inputQuiz.setPlayerName(name);

                            if (name.equalsIgnoreCase(inputQuiz.getPlayerChoosingCategory()))
                            {
                                categoryGUI.setCategories(inputQuiz.getTriviaCategories());
                                getToCategory.setVisible(true);
                                // TODO Kategori slumpas fram. Ersätts med GUI.
                                //inputQuiz.setCategory(randomCategory());
                                printCategories(inputQuiz.getTriviaCategories());
                                inputQuiz.setCategory(Integer.parseInt(JOptionPane.showInputDialog(name+" is Choosing Category.")));
                                //inputQuiz.setCategory();//Skicka in Category id.
                            }
                        }
                        System.out.println("PlayerName printer" + inputQuiz.getPlayerName() + " " + inputQuiz.getCategory());
                        out.writeObject(inputQuiz); //Test. Efter readOnly skickas båda samtidigt och player 1 går alltid först.
                        break;
                    }
                }
                pack();
            }
        } catch (IOException | ClassNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }

    }

    private void showNextQuest(Quiz inputquiz) throws IOException
    {
        if (inputquiz != null && currentQuestion < inputquiz.getAllQuestions().size()) {
            List<String> question = inputquiz.getAllQuestions().get(currentQuestion);
            bottompanel.removeAll();
            bottompanel.add(questionGUI);
            questionGUI.printQuestion(question);
            questionGUI.reset();
            currentQuestion++;
            revalidate();
            repaint();
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

    private void printCategories(List<TriviaCategory> triviaCategories) { //TODO test metod för val of kategori. Ersätt med GUI.
        for (TriviaCategory triviaCategory : triviaCategories) {
            System.out.println(triviaCategory.getName()+" Nr: "+triviaCategory.getId());
        }

    }

    private int randomCategory(){ //TODO Test. Metod för att slumpa fram en kategori. Ta bort.
        return (int)(Math.random()*(32 - 9 + 1)) + 9;
    }

    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == getToCategory){
            startGUI.setVisible(false);
            bottompanel.add(categoryGUI);
            categoryGUI.setVisible(true);
            getToQuestion.setVisible(true);
            getToCategory.setVisible(false);

        }

       if (e.getSource() == getToQuestion){
            categoryGUI.setVisible(false);
            bottompanel.add(questionGUI);
            questionGUI.setVisible(true);
           getToScoreTable.setVisible(false);
           getToQuestion.setVisible(true);

        }
       /*else if(e.getSource() == getToScoreTable){
           getToQuestion.setVisible(false);
           questionGUI.setVisible(false);
           getToCategory.setVisible(false);
       }*/
    }
}
