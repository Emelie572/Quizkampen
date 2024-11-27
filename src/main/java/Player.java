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
import java.util.List;

public class Player extends JFrame implements ActionListener
{
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader input;
    private String name;
    private Quiz playerOutputQuiz;

    private JPanel bottompanel = new JPanel();
    JPanel buttonSpace = new JPanel();
    JButton getToCategory = new JButton("Category");
    JButton getToQuestion = new JButton("Questions");
    JButton getToStartGUI = new JButton("Answer");
    int currentQuestion = 0;

    StartGUI startGUI;
    CategoryGUI categoryGUI;
    QuestionGUI questionGUI;

    Player() throws UnknownHostException
    {
        startGUI = new StartGUI();
        categoryGUI = new CategoryGUI(this);
        questionGUI = new QuestionGUI(this);

        getToStartGUI.addActionListener(e->{
            startGUI.setVisible(true);
            categoryGUI.setVisible(false);
            getToCategory.setVisible(false);
            getToQuestion.setVisible(false);});

        getToCategory.addActionListener(e->{
            categoryGUI.setVisible(true);
            startGUI.setVisible(false);
            questionGUI.setVisible(false);
            getToCategory.setVisible(false);
            getToQuestion.setVisible(false);});

        getToQuestion.addActionListener(e->{
            startGUI.setVisible(false);
            categoryGUI.setVisible(false);
            questionGUI.setVisible(true);
            getToQuestion.setVisible(true);});

        getToCategory.setPreferredSize(new Dimension(70, 50));
        getToCategory.setOpaque(true);
        getToCategory.setBorder(new LineBorder(Color.BLACK, 1, true));

        setTitle("Quizkampen");

        buttonSpace.add(getToCategory);
        buttonSpace.add(getToQuestion);
        getToQuestion.setVisible(false);
        setLayout(new BorderLayout());

        add(buttonSpace, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        categoryGUI.setVisible(false);
        questionGUI.setVisible(false);
        bottompanel.add(startGUI);
        bottompanel.add(categoryGUI);
        bottompanel.add(questionGUI);
        setSize(400, 300);
        add(bottompanel, BorderLayout.CENTER);

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

            Quiz connectionQuiz = new Quiz(name);
            connectionQuiz.setPlayerName(name);
            out.writeObject(connectionQuiz);
            Object inputLine;

            while (true)
            {
                while ((inputLine = in.readObject()) != null)
                {
                    if (inputLine instanceof Quiz) {

                        Quiz inputQuiz = (Quiz) inputLine;

                        if (!inputQuiz.isReadOnly()) {
                            getToQuestion.doClick();
                            inputQuiz.setPlayerName(name);
                            questionGUI.setAllquestions(inputQuiz.getAllQuestions());
                            questionGUI.printQuestion();
                            System.out.println("Frågor klara");

                        } else {//readOnly

                            if(inputQuiz.getScoreTable()!=null){
                                System.out.println(inputQuiz.getScoreTable().toString());
                            }
                            inputQuiz.setPlayerName(name);
                            if (name.equalsIgnoreCase(inputQuiz.getPlayerChoosingCategory()))
                            {
                                categoryGUI.setCategories(inputQuiz.getTriviaCategories());
                                getToCategory.doClick();
                            }else {
                                out.writeObject(inputQuiz);
                            }
                        }
                        //System.out.println("PlayerName printer" + inputQuiz.getPlayerName() + " " + inputQuiz.getCategory());
                        playerOutputQuiz = inputQuiz;
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


    @Override
    public void actionPerformed(ActionEvent e)
    {
            System.out.println(e.getActionCommand());
            if(playerOutputQuiz.triviaContains(e.getActionCommand())){
                try {
                    playerOutputQuiz.setCategoryString(e.getActionCommand());
                    out.writeObject(playerOutputQuiz);
                    getToQuestion.doClick();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
            if (e.getActionCommand().equals("COMPLETE")){

                try {
                    playerOutputQuiz.addToCorrectAnswers(questionGUI.numberOfCorrectAnswers);
                    out.writeObject(playerOutputQuiz);
                    getToCategory.doClick();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        new Player();
    }
}

/*
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

     private void showNextQuest(Quiz inputquiz) throws IOException
    {
        if (inputquiz != null && currentQuestion < inputquiz.getAllQuestions().size()) {
            List<String> question = inputquiz.getAllQuestions().get(currentQuestion);
            bottompanel.removeAll();
            bottompanel.add(questionGUI);
            //questionGUI.printQuestion(question); test
            questionGUI.reset();
            currentQuestion++;
            revalidate();
            repaint();
        }
    }


    private void printCategories(List<TriviaCategory> triviaCategories) { //TODO test metod för val of kategori. Ersätt med GUI.
        for (TriviaCategory triviaCategory : triviaCategories) {
            System.out.println(triviaCategory.getName()+" Nr: "+triviaCategory.getId());
        }

    }
 */