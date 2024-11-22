import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class QuestionGUI extends JPanel implements ActionListener
{
    JButton[] answers = new JButton[4];
    JTextField textField1 = new JTextField(20);
    String fråga = "Vilken färg är himlen?";
    private int seconds = 20;
    private JProgressBar progressBar = new JProgressBar(0, 20);
    private Timer timer;

    QuestionGUI()
    {
        //progressBar.setStringPainted(true);
        progressBar.setValue(20);

        add(progressBar);
        add(new Label("kort 2"));
        add(textField1);
        textField1.setText(fråga);

        java.util.List<String> color = Arrays.asList("gul", "grön", "blå", "röd");

        for (int i = 0; i < answers.length; i++)
        {
            answers[i] = new JButton(color.get(i));
            add(answers[i]);
            answers[i].addActionListener(this);
        }


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
    }

    public void reset()
    {
        seconds = 20;
        progressBar.setValue(seconds);
        for(JButton b : answers){
            b.setBackground(Color.white);
        }
        timer.start();
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

    public boolean checkAnswer(JButton button)
    {
        timer.stop();
        if (button.getText().equals("blå"))
        {
            button.setBackground(Color.green);
            return true;
        } else
        {
            button.setBackground(Color.red);
            return false;
        }
    }
}
