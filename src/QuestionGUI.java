import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuestionGUI extends JPanel implements ActionListener
{
    JButton[] answers = new JButton[4];
    JLabel questionLabel = new JLabel();
    JPanel bottomPanel = new JPanel(new GridLayout(3,1));
    JPanel top = new JPanel();
    JPanel center = new JPanel();
    JPanel bottom = new JPanel();
    String correctAnswer;
    private int seconds = 20;
    private JProgressBar progressBar = new JProgressBar(0, 20);
    private Timer timer;

    QuestionGUI()
    {

        progressBar.setValue(20);
        top.add(questionLabel);
        bottom.add(progressBar, BorderLayout.SOUTH);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 15));


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
        timer.start();

        for (int i = 0; i < answers.length; i++) {
            answers[i] = new JButton();
            answers[i].addActionListener(this);
            center.add(answers[i]);
            answers[i].setPreferredSize(new Dimension(60, 50));
            answers[i].setOpaque(true);
            answers[i].setBorder(new LineBorder(Color.BLUE,1, true));
        }
        bottomPanel.add(top, BorderLayout.NORTH);
        bottomPanel.add(center, BorderLayout.CENTER);
        bottomPanel.add(bottom, BorderLayout.SOUTH);
        bottomPanel.setBackground(Color.CYAN);
        add(bottomPanel);
    }

    public void reset()
    {
        for (int i = 0; i < answers.length; i++) {
            answers[i].setBackground(null);
        }
        seconds = 20;
        progressBar.setValue(seconds);
        timer.restart();
    }

    public void printQuestion(java.util.List<String> question) throws IOException
    {
        this.questionLabel.setText(question.get(0));
        for (int i = 0; i < answers.length; i++)
        {
            answers[i].setText(question.get(i+1));
        }
        correctAnswer = question.getLast();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == answers[0])
        {
            checkAnswer(answers[0]);
        } else if (e.getSource() == answers[1])
        {
            checkAnswer(answers[1]);

        } else if (e.getSource() == answers[2])
        {
            checkAnswer(answers[2]);

        } else if (e.getSource() == answers[3])
        {
            checkAnswer(answers[3]);
        }
    }

    public void checkAnswer(JButton button)
    {
        timer.stop();
        if (button.getText().equals(correctAnswer))
        {
            button.setBackground(Color.green);
        } else
        {
            button.setBackground(Color.red);
        }
    }
}
