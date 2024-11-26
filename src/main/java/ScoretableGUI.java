
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ScoretableGUI extends JFrame {


    private JPanel mainContentPanel;
    private JButton continueQuizButton;
    private JFrame frame;


    public ScoretableGUI(String player1, String player2, int rounds) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        frame.setBackground(Color.blue);

        setLayout(new BorderLayout());
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        add(mainContentPanel, BorderLayout.CENTER);

        addHeader(player1,player2); //någon metod som skapar header

        JLabel scoreResultTitel = new JLabel();
        scoreResultTitel.setFont(new Font("Arial", Font.BOLD, 15));
        add(scoreResultTitel, BorderLayout.NORTH);

        JPanel bottomButtonPanel = new JPanel();
        continueQuizButton = new JButton("Fortsätt");
        bottomButtonPanel.add(continueQuizButton);
        add(continueQuizButton, BorderLayout.SOUTH);
        continueQuizButton.setBackground(Color.WHITE);
        continueQuizButton.setPreferredSize(new Dimension(50, 40));
        continueQuizButton.setBorder(new LineBorder(Color.BLUE,1, true));
        //lambauttryck /knapp för att fortsätta spelet?
        continueQuizButton.addActionListener(e ->());


        for (int i = 0; i < rounds; i++) {
            for(int i = 1; i <=rounds; i++) {
                addRounds(); //någon metod som lägger till rader för varje rond, och typ har en "dolt" text innan

            }



        }

        //Lägg till header metod();

        //Lägg till rundor metod();

        //Lägg till en tabell eller celler metod();

        //Metod för att uppdatera poängen

        //lägg till en knapp som fortsätter spelet


/*design för knappar
    buttons[i].setBackground(Color.WHITE);
    buttons[i].setPreferredSize(new Dimension(50, 40));
    buttons[i].
