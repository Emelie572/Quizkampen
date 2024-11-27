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
    //Development
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

        getToCategory.setVisible(true);
        try (Socket socket = new Socket(ip, port);)
        {
            input = new BufferedReader(new InputStreamReader(System.in));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println(name+ " hej");

            //int testCategory = 10;//Test. Kategori input, Books
            int testCategory = randomCategory(); //Test. Slumpar fram en kategori
            Quiz connectionQuiz = new Quiz(testCategory, true);
            connectionQuiz.playerName = name;//Test. Sätter namn på quiz.
            out.writeObject(connectionQuiz);
            Object inputLine;

            while (true)
            {
                while ((inputLine = in.readObject()) != null)
                {
                    if (inputLine instanceof HashMap<?,?>){
                        List categoryList=null;
                        for(Object key: ((HashMap<?, ?>) inputLine).keySet()){
                            categoryList= new ArrayList(List.of(key));
                        }
                        categoryGUI.setCategories(categoryList);
                    }

                    if (inputLine instanceof Quiz)
                    {
                        Quiz inputQuiz = (Quiz) inputLine;

                        if (!inputQuiz.readOnly)
                        {
                            List<String> question;

                            for (int i = 0; i < inputQuiz.allQuestions.size(); i++)
                            {
                                question = inputQuiz.allQuestions.get(i);
                                printQuestion(question);
                                    questionGUI.reset();
                                    questionGUI.printQuestion(inputQuiz.allQuestions.get(i));
                                    questionGUI.reset();
                                String inputAnswer = input.readLine().trim();
                                inputQuiz.correctAnswers += checkAnswer(inputAnswer, question);
                            }
                            inputQuiz.playerName = name;
                        } else
                        { //readOnly
                            System.out.println(inputQuiz.scoreTable.toString());
                            System.out.println("Player choosing category: " + inputQuiz.playerChoosingCategory);
                            //inputQuiz.setCategory(randomCategory());//Test. Kategori ska sättas.
                            inputQuiz.playerName = name;//Test.
                            if (name.equalsIgnoreCase(inputQuiz.playerChoosingCategory))
                            {
                                inputQuiz.setCategory(Integer.parseInt(JOptionPane.showInputDialog(inputQuiz.playerName + " Nummer mellan 9-32")));//Test.
                            }
                        }
                        System.out.println("PlayerName printer" + inputQuiz.playerName + " " + inputQuiz.category);
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
        if (inputquiz != null && currentQuestion < inputquiz.allQuestions.size()) {
            List<String> question = inputquiz.allQuestions.get(currentQuestion);
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
            System.out.println("Rätt!\n");
            return 1;

        } else
        {
            System.out.println("Fel!\n");
        }
        return 0;
    }

    public void printQuestion(List<String> question) throws IOException
    {
        for (int i = 0; i < question.size() - 1; i++)
        {
            String rewrite = question.get(i).replaceAll("&#039;", "'");
            rewrite = rewrite.replaceAll("&quot;", "\"");
            rewrite = rewrite.replaceAll("&amp;", "&");
            System.out.print(rewrite + ", ");
        }
        System.out.println();
    }

    private int randomCategory()
    { //Test. Metod för att slumpa fram en kategori
        int randomCategory = (int) (Math.random() * (32 - 9 + 1)) + 9;
        System.out.println(randomCategory);
        return randomCategory;
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
