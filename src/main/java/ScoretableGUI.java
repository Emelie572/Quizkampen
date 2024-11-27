
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Map;

public class ScoretableGUI extends JFrame {

    private JPanel mainContentPanel;
    private JButton continueQuizButton;
    private ScoreTable scoretable;
    private String ROUNDTITEL = "Round";
    private String CONCEALEDROUND = "Dolt";
    private String SCORETABLETITEL = "Spelresultat";
    private String CONTINUEBUTTONTEXT = "Fortsätt";
    //private JFrame frame;


    public ScoretableGUI(String player1, String player2, int rounds, ScoreTable scoretable) {
        this.scoretable = scoretable;


        setLayout(new BorderLayout());
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        add(mainContentPanel, BorderLayout.CENTER);

        headerForColumns(player1, player2);

        for (int i = 1; i < rounds; i++) {
            createRoundRow(i);

            JLabel scoreResultTitel = new JLabel(SCORETABLETITEL, SwingConstants.CENTER);
            scoreResultTitel.setFont(new Font("Arial", Font.BOLD, 15));
            add(scoreResultTitel, BorderLayout.NORTH);

            JPanel bottomButtonPanel = new JPanel();
            continueQuizButton = new JButton(CONTINUEBUTTONTEXT);
            bottomButtonPanel.add(continueQuizButton);
            add(continueQuizButton, BorderLayout.SOUTH);
            continueQuizButton.setBackground(Color.WHITE);
            continueQuizButton.setPreferredSize(new Dimension(50, 40));
            continueQuizButton.setBorder(new LineBorder(Color.BLUE, 1, true));
            //lambauttryck /knapp för att fortsätta spelet?
            //  continueQuizButton.addActionListener(e -> här behövs metod för knapptryck());

        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

    }

    private void headerForColumns(String player1, String player2) {
        JPanel headerForColumns = new JPanel(new GridLayout(1, 3));
        headerForColumns.add(createSquare(player1, Font.BOLD));
        headerForColumns.add(createSquare(ROUNDTITEL, Font.BOLD));
        headerForColumns.add(createSquare(player2, Font.BOLD));
        mainContentPanel.add(headerForColumns);
    }

    private void createRoundRow(int round) {
        JPanel roundRow = new JPanel(new GridLayout(1, 3));
        JLabel player1Label = createSquare(CONCEALEDROUND, Font.PLAIN);
        player1Label.setName("Spelare 1");
        roundRow.add(player1Label);

        roundRow.add(createSquare("Runda " + round, Font.PLAIN));

        JLabel player2Label = createSquare(CONCEALEDROUND, Font.PLAIN);
        player2Label.setName("Spelare 2");
        roundRow.add(player2Label);

        mainContentPanel.add(roundRow);
    }

    private JLabel createSquare(String text, int fontStyle) {
        JLabel squareLabel = new JLabel(text, SwingConstants.CENTER);
        squareLabel.setFont(new Font("Arial", fontStyle, 15));
        squareLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return squareLabel;
    }

    public void updatedScoreTable() {
        for (int round = 1; round < scoretable.gameScore.size(); round++) {
            Map<String, Integer> roundscores = scoretable.gameScore.get(round);
            JPanel roundRow = (JPanel) mainContentPanel.getComponent(round);

            JLabel player1 = (JLabel) roundRow.getComponent(0);
            String playerOneName = player1.getName();
            Integer playarOneScore = roundscores.get(playerOneName);
            player1.setText(playarOneScore == null ? CONCEALEDROUND : String.valueOf(playarOneScore));

            JLabel player2 = (JLabel) roundRow.getComponent(2);
            String playerTwoName = player2.getName();
            Integer playerTwoScore = roundscores.get(playerTwoName);
            player2.setText(playerTwoScore == null ? CONCEALEDROUND : String.valueOf(playerTwoScore));
        }
    }
}

//Lägg till header metod();

//Lägg till rundor metod();

//Lägg till en tabell eller celler metod();

//Metod för att uppdatera poängen

//lägg till en knapp som fortsätter spelet

//Lägg till resultat rad för båda spelarna


