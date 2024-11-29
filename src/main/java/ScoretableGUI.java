
import javax.swing.*;
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
    private final String ROUND_TITEL = "Spelomgång";
    private final String CONCEALED_ROUND = "Dolt";
    private final String WINNER_MESSAGE = " vann! ";
    private final String TIE_MESSAGE = "Oavgjort! ";
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final Color WHITE_COLOR = new Color(255, 255, 255);
    private int roundsPlayed = 0;

    public ScoretableGUI() {

        setLayout(new BorderLayout());
        setBackground(BLUE_COLOR);


        createResultPanel();

    }

        private void createMainContent() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(BLUE_COLOR);

        JPanel header = new JPanel(new GridLayout(1, 3));

            header.setBackground(BLUE_COLOR);
            header.add(createSquare(player1.replaceAll("[^a-zA-Z]",""), Font.PLAIN));
            header.add(createSquare(ROUND_TITEL, Font.PLAIN));
            header.add(createSquare(player2.replaceAll("[^a-zA-Z]",""), Font.PLAIN));
            header.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

            mainContentPanel.add(header);

            for (int i = 0; i < rounds; i++) {
            createRoundRow(i+1, mainContentPanel);
        }

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(BLUE_COLOR);

        resultLabelMessage = new JLabel("", SwingConstants.CENTER);
        resultLabelMessage.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabelMessage.setForeground(WHITE_COLOR);
        resultLabelMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultLabelResult = new JLabel("", SwingConstants.CENTER);
        resultLabelResult.setFont(new Font("Arial", Font.PLAIN, 15));
        resultLabelResult.setForeground(WHITE_COLOR);
        resultLabelResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultPanel.add(resultLabelMessage, BorderLayout.NORTH);
        resultPanel.add(resultLabelResult, BorderLayout.SOUTH);

        resultPanel.add(Box.createVerticalStrut(20));
        resultPanel.add(resultLabelMessage);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(resultLabelResult);

    }


    private void createRoundRow(int round, JPanel mainContentPanel) {
        JPanel roundRow = new JPanel(new GridLayout(1, 3));
        roundRow.setBackground(BLUE_COLOR);
        roundRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
        roundsPlayed++;

        player1 = scoreTable.getPlayerNames().getFirst();
        player2 = scoreTable.getPlayerNames().getLast();
        rounds = scoreTable.getGameScore().size()-1;

        if(mainContentPanel==null){
            createMainContent();
        }

        for (int round = 1; round < scoreTable.getGameScore().size(); round++) {
            Map<String, Integer> roundScores = scoreTable.getGameScore().get(round);
            JPanel roundRow = (JPanel) mainContentPanel.getComponent(round);

            JLabel player1Label = (JLabel) roundRow.getComponent(0);
            Integer playerOneScore = roundScores.get(scoreTable.getPlayerNames().getFirst());
            player1Label.setText(playerOneScore == null ? CONCEALED_ROUND : String.valueOf(playerOneScore));

            playerOneTotalScore += playerOneScore == null ? 0 : playerOneScore;

            JLabel player2Label = (JLabel) roundRow.getComponent(2);
            Integer playerTwoScore = roundScores.get(scoreTable.getPlayerNames().getLast());
            player2Label.setText(playerTwoScore == null ? CONCEALED_ROUND : String.valueOf(playerTwoScore));

            playerTwoTotalScore += playerTwoScore == null ? 0 : playerTwoScore;

            if (roundsPlayed == rounds) {
                add(resultPanel, BorderLayout.SOUTH);

                if (playerOneTotalScore > playerTwoTotalScore) {
                    resultLabelMessage.setText(player1.replaceAll("[^a-zA-Z]","") + WINNER_MESSAGE);
                } else if (playerOneTotalScore < playerTwoTotalScore) {
                    resultLabelMessage.setText(player2.replaceAll("[^a-zA-Z]","") + WINNER_MESSAGE);
                } else {
                    resultLabelMessage.setText(TIE_MESSAGE);
                }

                resultLabelResult.setText("Poängställning: " + playerOneTotalScore + " - " + playerTwoTotalScore);
            }

            revalidate();
            repaint();


        }

    }
}
