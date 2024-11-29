import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player extends JFrame implements ActionListener
{
    private InetAddress ip = InetAddress.getLocalHost();
    private int port = 3400;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String name;
    private Quiz playerOutputQuiz;

    private final JPanel leftPanel = new JPanel();
    private final JPanel buttonSpace = new JPanel();
    private JPanel rightPanel = new JPanel();

    private int currentQuestion = 0;
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final StartGUI startGUI;
    private final CategoryGUI categoryGUI;
    private final QuestionGUI questionGUI;
    private final ScoretableGUI scoretabelGUI;

    Player() throws UnknownHostException
    {
        setTitle("Quizkampen");
        setLayout(new BorderLayout());
        setBackground(BLUE_COLOR);
        setSize(700, 500);

        leftPanel.setBackground(BLUE_COLOR);
        buttonSpace.setBackground(BLUE_COLOR);

        scoretabelGUI = new ScoretableGUI();
        startGUI = new StartGUI();
        categoryGUI = new CategoryGUI(this);
        categoryGUI.setVisible(false);
        questionGUI = new QuestionGUI(this);
        questionGUI.setVisible(false);
       // this.questionGUI.setMaximumSize(new Dimension(100, 100));


        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(startGUI);
        leftPanel.add(categoryGUI);
        leftPanel.add(questionGUI);
        leftPanel.setBackground(BLUE_COLOR);

        // leftPanel.setMaximumSize(new Dimension(200, 50));

        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scoretabelGUI, BorderLayout.NORTH);
        rightPanel.setBackground(BLUE_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));
       // rightPanel.setPreferredSize(new Dimension(200, 500));

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);


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
                    if (inputLine instanceof Quiz inputQuiz) {

                        if (!inputQuiz.isReadOnly()) {
                            inputQuiz.setPlayerName(name);
                            getToQuestion();
                            questionGUI.setAllQuestions(inputQuiz.getAllQuestions(),inputQuiz.getCategoryString());
                            questionGUI.printQuestion();

                        } else {
                            //readOnly
                            if(!startGUI.HaveOpponent()) {
                                for (String opponent : inputQuiz.getPlayersInGame()) {
                                    if (!opponent.equals(name)) {
                                        startGUI.waitingForPlayerLable(opponent);
                                    }
                                }
                            }
                            inputQuiz.setPlayerName(name);
                            if(inputQuiz.getScoreTable()!=null){
                                scoretabelGUI.updatedScoreTable(inputQuiz.getScoreTable());
                            }
                            if(inputQuiz.isEndOfGame()){
                                startGUI.setVisible(false);
                                categoryGUI.setVisible(false);
                                questionGUI.setVisible(false);
                                pack();
                                System.out.println("END OF GAME");
                                inputQuiz = new Quiz();

                            }
                            if (name.equalsIgnoreCase(inputQuiz.getPlayerChoosingCategory())){
                                categoryGUI.setCategories(inputQuiz.getTriviaCategories());
                                getToCategory();
                            }else{
                                out.writeObject(inputQuiz);
                            }
                        }
                        playerOutputQuiz = inputQuiz;
                        break; //TODO behövs den?
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
            if(playerOutputQuiz.triviaContains(e.getActionCommand())){ //ActionEvent från CategoryGUI
                try {
                    playerOutputQuiz.setCategoryString(e.getActionCommand());
                    out.writeObject(playerOutputQuiz);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
            if (e.getActionCommand().equals("COMPLETE")){ //ActionEvent från QuestionGUI
                try {
                    playerOutputQuiz.addToCorrectAnswers(questionGUI.numberOfCorrectAnswers);
                    getToStartGUI();
                    out.writeObject(playerOutputQuiz);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
    }
    public void getToStartGUI() {
        startGUI.setVisible(true);
        categoryGUI.setVisible(false);
        questionGUI.setVisible(false);
    }
    public void getToCategory() {
        startGUI.setVisible(false);
        categoryGUI.setVisible(true);
        questionGUI.setVisible(false);
    }
    public void getToQuestion() {
        startGUI.setVisible(false);
        categoryGUI.setVisible(false);
        questionGUI.setVisible(true);
    }
    public static void main(String[] args) throws UnknownHostException {new Player();}
}