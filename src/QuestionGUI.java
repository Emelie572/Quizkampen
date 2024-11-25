import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class QuestionGUI extends JPanel implements ActionListener
{
    JButton[] answers = new JButton[4];
    JLabel question = new JLabel();
    JPanel bottomPanel = new JPanel(new BorderLayout());
    String correctAnswer;
    private int seconds = 20;
    private JProgressBar progressBar = new JProgressBar(0, 20);
    private Timer timer;

    QuestionGUI()
    {
        progressBar.setValue(20);
        add(question);
        add(progressBar);

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
            add(answers[i]);

        }
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
        this.question.setText(question.get(0));
        for (int i = 0; i < answers.length; i++)
        {
            answers[i].setText(question.get(i+1));
        }
        correctAnswer = question.getLast();
    }

    public void m(java.util.List<String> question) throws IOException
    {
        this.question.setText(question.get(0));
        for (int i = 0; i < answers.length; i++)
        {
            answers[i].setText(question.get(i + 1));
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
