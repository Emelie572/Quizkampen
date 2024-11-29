import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuestionGUI extends JPanel implements ActionListener
{

    private final JLabel questionLabel = new JLabel();
    private final JPanel centerPanel = new JPanel(new GridLayout(3,1));
    private final JLabel categoryLabel = new JLabel(String.valueOf(SwingConstants.CENTER));
    private final JPanel top = new JPanel(new GridLayout(2,1));
    private final JPanel center = new JPanel(new GridLayout(4,1));
    private final JPanel bottom = new JPanel();
    private final JProgressBar progressBar = new JProgressBar(0, 20);
    private final Timer timer;
    private final JButton play = new JButton("Next Question");
    private final JButton notVisibleButton = new JButton("COMPLETE");
    private final JButton[] answers = new JButton[4];
    List<List<String>> list = new ArrayList<>();
    private String category;
    private int seconds = 20;
    int numberOfCorrectAnswers;
    int questionNumber = 0;
    private final Color BLUE_COLOR = new Color(30, 70, 150);

    QuestionGUI(ActionListener playerListener)
    {

        setBackground(BLUE_COLOR);
        notVisibleButton.addActionListener(playerListener);
        progressBar.setValue(20);
        top.setSize(new Dimension(100,50));

        top.add(categoryLabel);
        top.add(questionLabel);

        questionLabel.setLayout(new BoxLayout(questionLabel, BoxLayout.Y_AXIS));


        bottom.add(progressBar, BorderLayout.SOUTH);
        bottom.add(play);
        bottom.setBackground(BLUE_COLOR);

        categoryLabel.setFont(new Font("Arial", Font.BOLD, 15));
        categoryLabel.setBorder(BorderFactory.createLineBorder(BLUE_COLOR, 2));
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setBackground(BLUE_COLOR);
        categoryLabel.setOpaque(true);
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionLabel.setFont(new Font("Arial", Font.BOLD, 15));
        questionLabel.setBorder(BorderFactory.createLineBorder(BLUE_COLOR, 2));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBackground(BLUE_COLOR);
        questionLabel.setOpaque(true);
        questionLabel.setOpaque(true);

        play.addActionListener(e-> nextQuestion());
        play.setForeground(BLUE_COLOR);
        play.setBackground(Color.WHITE);

        progressBar.setBackground(BLUE_COLOR);
        top.setBackground(BLUE_COLOR);


        timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (seconds > 0)
                {
                    seconds--;
                    progressBar.setValue(seconds);
                }
            }
        });

        for (int i = 0; i < answers.length; i++) {
            answers[i] = new JButton();
            answers[i].addActionListener(this);
            center.add(answers[i]);
            answers[i].setPreferredSize(new Dimension(60,35));
            answers[i].setBorder(new LineBorder(BLUE_COLOR,1, true));
            answers[i].setOpaque(true);
        }
        centerPanel.add(top, BorderLayout.NORTH);
        centerPanel.add(center, BorderLayout.CENTER);
        centerPanel.add(bottom, BorderLayout.SOUTH);
        centerPanel.setSize(600,400);
        add(centerPanel);
        setMinimumSize(new Dimension(400, 300));
    }

    public void setAllQuestions(List<List<String>> allQuestions, String category){
        categoryLabel.setText(category);
        play.setText("Next Question");
        this.category = category;
        this.list = allQuestions;
        play.setText("Next Question");
        timer.start();
    }

    public void printQuestion() throws IOException {

        List<String>question = list.get(questionNumber);
        play.setVisible(false);
        if (questionNumber ==list.size()-1){
            play.setText("End Round");
        }
        timer.restart();
        this.questionLabel.setText(question.getFirst());

        for (int i = 0; i < answers.length; i++) {
            answers[i].setText(question.get(i + 1));
            answers[i].setMinimumSize(new Dimension(60, 50));
            }
        }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource().equals(answers[0]))
        {
            checkAnswer(answers[0]);
            showAnswer();

        } else if (e.getSource().equals(answers[1]))
        {
            checkAnswer(answers[1]);
            showAnswer();

        } else if (e.getSource().equals(answers[2]))
        {
            checkAnswer(answers[2]);
            showAnswer();

        } else if (e.getSource().equals(answers[3]))
        {
            checkAnswer(answers[3]);
            showAnswer();

        }
    }
    public void checkAnswer(JButton button) {
        timer.stop();
        String correct = correctAnswerString(list.get(questionNumber));
        if (button.getText().equals(correct))
        {
            button.setBackground(Color.green);
            numberOfCorrectAnswers++;
        } else
        {
            button.setBackground(Color.red);
        }
    }
    public void showAnswer() {
        for (int i = 0; i < answers.length; i++)
        {
            String correct = correctAnswerString(list.get(questionNumber));
            if (answers[i].getText().equals(correct))
            {
                answers[i].setBackground(Color.green);
            }
            answers[i].setEnabled(false);
        }
        play.setVisible(true);
    }

    public String correctAnswerString (List<String> question){
        int correctAnswer = Integer.parseInt(question.getLast());
        return question.get(correctAnswer);
    }

    private void nextQuestion() {

        questionNumber++;
        if(questionNumber <list.size()){
            try {
                reset();
                printQuestion();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else {
            questionNumber =0;
            reset();
            notVisibleButton.doClick(1);
            numberOfCorrectAnswers=0;
        }
    }

    public void reset()
    {
        for (int i = 0; i < answers.length; i++) {
            answers[i].setBackground(null);
            answers[i].setEnabled(true);
        }
        seconds = 20;
        progressBar.setValue(seconds);
        timer.restart();
    }
}
