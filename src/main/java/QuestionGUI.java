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
    private final JPanel groundPanel = new JPanel(new BorderLayout());
    private final JPanel centerPanel = new JPanel(new GridLayout(3,1));
    private final JPanel bottomPanel = new JPanel();
    private final JPanel top = new JPanel();
    private final JPanel center = new JPanel();
    private final JPanel bottom = new JPanel();
    private final JProgressBar progressBar = new JProgressBar(0, 20);
    private final Timer timer;
    private final JButton play = new JButton("Next Question");
    private final JButton notVisibleButton = new JButton("COMPLETE");
    private final JButton[] answers = new JButton[4];
    List<List<String>> list = new ArrayList<>();
    private String correctAnswer;
    private int seconds = 20;
    int numberOfCorrectAnswers;
    int QuestionNumber = 0;

    QuestionGUI(ActionListener playerListener)
    {
        notVisibleButton.addActionListener(playerListener);
        progressBar.setValue(20);
        top.add(questionLabel);
        bottom.add(progressBar, BorderLayout.SOUTH);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        play.addActionListener(e-> selectAnswer());
        bottomPanel.add(play);



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
        //timer.start();

        for (int i = 0; i < answers.length; i++) {
            answers[i] = new JButton();
            answers[i].addActionListener(this);
            center.add(answers[i]);
            answers[i].setMinimumSize(new Dimension(60,50));
            answers[i].setBorder(new LineBorder(Color.BLUE,1, true));
        }
        centerPanel.add(top, BorderLayout.NORTH);
        centerPanel.add(center, BorderLayout.CENTER);
        centerPanel.add(bottom, BorderLayout.SOUTH);
        groundPanel.add(centerPanel, BorderLayout.CENTER);
        groundPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(groundPanel);
        setMinimumSize(new Dimension(400, 300));
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

    public void setAllQuestions(List<List<String>> allQuestions){
        this.list = allQuestions;
        timer.start();
    }

    public void printQuestion() throws IOException {

        List<String>question = list.get(QuestionNumber);
        play.setVisible(false);
        timer.restart();
        this.questionLabel.setText(question.getFirst());
        correctAnswer = question.getLast();

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
            checkAnswer(answers[0], list.get(QuestionNumber));
            showAnswer();

        } else if (e.getSource().equals(answers[1]))
        {
            checkAnswer(answers[1],list.get(QuestionNumber));
            showAnswer();

        } else if (e.getSource().equals(answers[2]))
        {
            checkAnswer(answers[2],list.get(QuestionNumber));
            showAnswer();

        } else if (e.getSource().equals(answers[3]))
        {
            checkAnswer(answers[3],list.get(QuestionNumber));
            showAnswer();

        }
    }

    public void checkAnswer(JButton button, List<String> question) {
        timer.stop();
        int correctAnswer = Integer.parseInt(question.getLast());
        String correct = question.get(correctAnswer).trim();
        if (button.getText().equals(correct))
        {
            button.setBackground(Color.green);
            numberOfCorrectAnswers++;
        } else
        {
            button.setBackground(Color.red);
        }
    }
    private void selectAnswer() {

        QuestionNumber++;
        if(QuestionNumber<list.size()){
            try {
                reset();
                printQuestion();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else {
            QuestionNumber=0;
            reset();
            notVisibleButton.doClick(1);
            numberOfCorrectAnswers=0;
        }
    }

    public void showAnswer()
    {
            for (int i = 0; i < answers.length; i++)
            {
                if (answers[i].getText().equals(correctAnswer))
                {
                    answers[i].setBackground(Color.green);
                }
                answers[i].setEnabled(false);
            }
            play.setVisible(true);
    }
}
