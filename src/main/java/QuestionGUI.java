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
    JPanel groundPanel = new JPanel(new BorderLayout());
    JPanel centerPanel = new JPanel(new GridLayout(3,1));
    JPanel bottomPanel = new JPanel();
    JButton play = new JButton("Play");
    JPanel top = new JPanel();
    JPanel center = new JPanel();
    JPanel bottom = new JPanel();
    String correctAnswer;
    private int seconds = 20;
    private JProgressBar progressBar = new JProgressBar(0, 20);
    private Timer timer;
    java.util.List<String> list = new java.util.ArrayList();

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
            //answers[i].setPreferredSize(new Dimension(60, 50));
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

    public void printQuestion(java.util.List<String> question) throws IOException
    {
        timer.restart();
        this.questionLabel.setText(question.get(0));
        for (int i = 0; i < answers.length; i++)
        {
            answers[i].setText(question.get(i+1));
            answers[i].setMinimumSize(new Dimension(60,50));
        }
        list =question;
        correctAnswer = question.getLast();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() == answers[0])
        {
            checkAnswer(answers[0], list);
                showAnswer();

        } else if (e.getSource() == answers[1])
        {
            checkAnswer(answers[1],list);
            showAnswer();

        } else if (e.getSource() == answers[2])
        {
            checkAnswer(answers[2],list);
            showAnswer();
        } else if (e.getSource() == answers[3])
        {
            checkAnswer(answers[3],list);
            showAnswer();
        }
    }

    public void checkAnswer(JButton button, java.util.List<String> question)
    {
        timer.stop();
        int correctAnswer = Integer.parseInt(question.getLast());
        String correct = question.get(correctAnswer).trim();
        if (button.getText().equals(correct))
        {
            button.setBackground(Color.green);
        } else
        {
            button.setBackground(Color.red);
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
                //answers[i].setEnabled(false);
            }
            play.setVisible(true);
    }

    /*public JButton getPlay(JButton play){
        this.play= play;
        return play;
    }

    public int getCounter(){
        return counter;
    }*/

}
