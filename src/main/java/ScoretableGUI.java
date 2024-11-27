
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ScoretableGUI extends JFrame {


    private JPanel mainContentPanel;
    private JButton continueQuizButton;
    private ScoreTable scoretable;
    private JFrame frame;
    private String ROUNDTITEL = "Round";
    private String CONCEALEDROUND = "Dolt";



    public ScoretableGUI(String player1, String player2, int rounds, ScoreTable scoretable) {
        this.scoretable = scoretable;

        setLayout(new BorderLayout());
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        add(mainContentPanel, BorderLayout.CENTER);

        headerForColumns(player1, player2);

        JLabel scoreResultTitel = new JLabel("Spelresultat");
        scoreResultTitel.setFont(new Font("Arial", Font.BOLD, 15));
        add(scoreResultTitel, BorderLayout.NORTH);

        JPanel bottomButtonPanel = new JPanel();
        continueQuizButton = new JButton("Fortsätt");
        bottomButtonPanel.add(continueQuizButton);
        add(continueQuizButton, BorderLayout.SOUTH);
        continueQuizButton.setBackground(Color.WHITE);
        continueQuizButton.setPreferredSize(new Dimension(50, 40));
        continueQuizButton.setBorder(new LineBorder(Color.BLUE, 1, true));
        //lambauttryck /knapp för att fortsätta spelet?
        continueQuizButton.addActionListener(e -> ());


        for (int i = 0; i < rounds; i++) {
            addRounds(); //någon metod som lägger till rader för varje rond, och typ har en "dolt" text innan

        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

    }

    private void headerForColumns(String player1, String player2) {
        JPanel headerForColumns = new JPanel(new GridLayout(1, 3));
        headerForColumns.add(createSquare(player1, Font.BOLD));
        headerForColumns.add(createSquare(ROUNDTITEL, Font.BOLD));
        headerForColumns.add(createSquare(player2, Font.BOLD));
        mainContentPanel.add(headerForColumns);
    }

    private JLabel createSquare(String text, int fontStyle) {
        JLabel squareLabel = new JLabel(text, SwingConstants.CENTER);
        squareLabel.setFont(new Font("Arial", fontStyle, 15));
        squareLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return squareLabel;
    }

    private void createRoundRow(int round) {
        JPanel roundRow = new JPanel(new GridLayout(1, 3));
        roundRow.add(createSquare(CONCEALEDROUND,Font.PLAIN));
        roundRow.add(createSquare(ROUNDTITEL,round), Font.PLAIN);
        roundRow.add(createSquare(CONCEALEDROUND,Font.PLAIN));
        mainContentPanel.add(roundRow);
    }
}
        //Lägg till header metod();

        //Lägg till rundor metod();

        //Lägg till en tabell eller celler metod();

        //Metod för att uppdatera poängen

        //lägg till en knapp som fortsätter spelet



