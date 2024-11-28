
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Map;

public class ScoretableGUI extends JPanel {

    private JPanel mainContentPanel;
    private JPanel resultPanel;
    private JLabel resultLabelMessage;
    private JLabel resultLabelResult;
    private ScoreTable scoretable;
    private String player1;
    private String player2;
    private  int rounds;
    private final String ROUND_TITEL = "Spelresultat";
    private final String CONCEALED_ROUND = "Dolt";
    private final String WINNER_MESSAGE = " vann! ";
    private final String LOSER_MESSAGE = " förlorade! ";
    private final String TIE_MESSAGE = "Oavgjort! ";
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final Color WHITE_COLOR = new Color(255, 255, 255);

    public ScoretableGUI() {

        setLayout(new BorderLayout());
        setBackground(BLUE_COLOR);

        createResultPanel();
        createMainContent();

    }

        private void createMainContent() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(BLUE_COLOR);

        headerForColumns(player1, player2, mainContentPanel);

        for (int i = 1; i <= rounds; i++) {
            createRoundRow(i, mainContentPanel);
        }

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void createResultPanel() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(BLUE_COLOR);

        resultLabelMessage = new JLabel("", SwingConstants.CENTER);
        resultLabelMessage.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabelMessage.setForeground(WHITE_COLOR);

        resultLabelResult = new JLabel("", SwingConstants.CENTER);
        resultLabelResult.setFont(new Font("Arial", Font.PLAIN, 15));
        resultLabelResult.setForeground(WHITE_COLOR);

        resultPanel.add(resultLabelMessage, BorderLayout.NORTH);
        resultPanel.add(resultLabelResult, BorderLayout.SOUTH);

    }

    private void headerForColumns(String player1, String player2, JPanel mainContentPanel) {
        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setBackground(BLUE_COLOR);
        header.add(createSquare(player1, Font.PLAIN));
        header.add(createSquare(ROUND_TITEL, Font.PLAIN));
        header.add(createSquare(player2, Font.PLAIN));
        mainContentPanel.add(header);
    }

    private void createRoundRow(int round, JPanel mainContentPanel) {
        JPanel roundRow = new JPanel(new GridLayout(1, 3));
        roundRow.setBackground(BLUE_COLOR);
        roundRow.add(createSquare(CONCEALED_ROUND, Font.ITALIC));
        roundRow.add(createSquare(String.valueOf(round), Font.PLAIN));
        roundRow.add(createSquare(CONCEALED_ROUND, Font.ITALIC));
        mainContentPanel.add(roundRow);
    }

    private JLabel createSquare(String text, int fontStyle) {
        JLabel squareLabel = new JLabel(text, SwingConstants.CENTER);
        squareLabel.setFont(new Font("Arial", fontStyle, 16));
        squareLabel.setForeground(WHITE_COLOR);
        return squareLabel;
    }

    public void updatedScoreTable(ScoreTable scoreTable) {
        int playerOneTotalScore = 0;
        int playerTwoTotalScore = 0;

        for (int round = 0; round < scoreTable.getGameScore().size(); round++) {
            Map<String, Integer> roundScores = scoreTable.getGameScore().get(round);
            JPanel roundRow = (JPanel) mainContentPanel.getComponent(round);

            JLabel player1Label = (JLabel) roundRow.getComponent(0);
            String playerOneName = player1Label.getName();
            Integer playerOneScore = roundScores.get(playerOneName);
            player1Label.setText(playerOneScore == null ? CONCEALED_ROUND : String.valueOf(playerOneScore));

            playerOneTotalScore += playerOneScore == null ? 0 : playerOneScore;

            JLabel player2Label = (JLabel) roundRow.getComponent(2);
            String playerTwoName = player2Label.getName();
            Integer playerTwoScore = roundScores.get(playerTwoName);
            player2Label.setText(playerTwoScore == null ? CONCEALED_ROUND : String.valueOf(playerTwoScore));

            playerTwoTotalScore += playerTwoScore == null ? 0 : playerTwoScore;

            if (scoreTable.getGameScore().size() == rounds) {
                add(resultPanel, BorderLayout.SOUTH);

                if (playerOneTotalScore > playerTwoTotalScore) {
                    resultLabelMessage.setText(playerOneName + WINNER_MESSAGE + playerTwoName);
                } else if (playerTwoTotalScore > playerOneTotalScore) {
                    resultLabelMessage.setText(playerTwoName + WINNER_MESSAGE + playerOneName);
                } else {
                    resultLabelMessage.setText(TIE_MESSAGE);
                }

                resultLabelResult.setText("Poängställning: " + playerOneTotalScore + " - " + playerTwoTotalScore);
                //fixa så att poängen visas rätt beronde på vilken spelare som visas först
            }

            revalidate();
            repaint();


        }


    }
}
