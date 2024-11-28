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
    private int port = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String name;
    private Quiz playerOutputQuiz;

    private final JPanel bottompanel = new JPanel();
    private final JPanel buttonSpace = new JPanel();
    private JPanel scorepanel = new JPanel();

    private int currentQuestion = 0;
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final StartGUI startGUI;
    private final CategoryGUI categoryGUI;
    private final QuestionGUI questionGUI;
    private final ScoretableGUI scoretabelGUI;

    Player() throws UnknownHostException
    {
        setTitle("Quizkampen");

        startGUI = new StartGUI();
        categoryGUI = new CategoryGUI(this);
        categoryGUI.setVisible(false);
        questionGUI = new QuestionGUI(this);
        questionGUI.setVisible(false);
        bottompanel.add(startGUI);
        bottompanel.add(categoryGUI);
        bottompanel.add(questionGUI);
        this.setLayout(new BorderLayout());
        this.add(bottompanel, BorderLayout.CENTER);
        this.add(buttonSpace, BorderLayout.SOUTH);
        //setSize(400, 300);
        setMinimumSize(new Dimension(1000, 400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        while (startGUI.getname() == null || startGUI.getname().isEmpty()) {
            try {
                Thread.sleep(100); //TODO testa utan
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
                            inputQuiz.setPlayerName(name);
                            if(inputQuiz.getScoreTable()!=null){
                                System.out.println(inputQuiz.getScoreTable().toString());//TODO Ersätt med GUI.
                            }
                            if (name.equalsIgnoreCase(inputQuiz.getPlayerChoosingCategory())){
                                categoryGUI.setCategories(inputQuiz.getTriviaCategories());
                                getToCategory();
                            }else{
                                if(inputQuiz.getPlayerChoosingCategory()!=null){ //TODO Flytta till StartGUI
                                    startGUI.waitingForPlayerLable(inputQuiz);
                                }
                                getToStartGUI();
                                out.writeObject(inputQuiz);
                            }
                        }
                        playerOutputQuiz = inputQuiz;
                        break; //TODO behövs den?
                    }
                }
                //pack();
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