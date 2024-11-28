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
    JButton[] answers = new JButton[4];
    JLabel questionLabel = new JLabel();
    JPanel groundPanel = new JPanel(new BorderLayout());
    JPanel centerPanel = new JPanel(new GridLayout(3,1));
    JPanel bottomPanel = new JPanel();
    JButton play = new JButton("Play");
    JPanel top = new JPanel();
    JPanel center = new JPanel(new GridLayout(4,1));
    JPanel bottom = new JPanel();
    String correctAnswer;
    private int seconds = 20;
    private JProgressBar progressBar = new JProgressBar(0, 20);
    private Timer timer;
    JButton complete = new JButton("COMPLETE");

    List<String>question;

    List<List<String>> list = new ArrayList<>();
    int numberOfCorrectAnswers;
    int QuestionNumber = 0;

    QuestionGUI(ActionListener playerListener)
    {
        complete.addActionListener(playerListener);
        progressBar.setValue(20);
        top.setSize(new Dimension(600,50));
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
            //test this annars
            int bi = i;
            answers[i].addActionListener(this);
            center.add(answers[i]);
            answers[i].setPreferredSize(new Dimension(60,35));
            answers[i].setBorder(new LineBorder(Color.BLUE,1, true));
        }
        centerPanel.add(top, BorderLayout.NORTH);
        centerPanel.add(center, BorderLayout.CENTER);
        centerPanel.add(bottom, BorderLayout.SOUTH);
        groundPanel.add(centerPanel, BorderLayout.CENTER);
        groundPanel.add(bottomPanel, BorderLayout.SOUTH);
        //top.setBackground(new Color(30, 70, 150));
        //bottom.setBackground(new Color(30, 70, 150));
        //center.setBackground(new Color(30, 70, 150));
        centerPanel.setSize(600,400);
        groundPanel.setSize(600, 400);
        add(groundPanel);

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

    public void setAllquestions(List<List<String>> allQuestions){
        this.list = allQuestions;
    }

    public void printQuestion() throws IOException {
        question = list.get(QuestionNumber);

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
        reset();
        if (e.getSource() == answers[0])
        {
            //checkAnswer(answers[0], list.get(QuestionNumber));
            //showAnswer();
            try
            {
                Thread.sleep(2000);
                checkAnswer(answers[0],list.get(QuestionNumber));
                showAnswer();

            } catch (InterruptedException ex)
            {
                throw new RuntimeException(ex);
            }
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
                complete.doClick();
                numberOfCorrectAnswers=0;
            }
        } else if (e.getSource() == answers[1])
        {
            //checkAnswer(answers[1],list.get(QuestionNumber));
            //showAnswer();
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
                complete.doClick();
                numberOfCorrectAnswers=0;
            }

        } else if (e.getSource() == answers[2])
        {
            checkAnswer(answers[2],list.get(QuestionNumber));
            showAnswer();
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
                complete.doClick();
                numberOfCorrectAnswers=0;
            }

        } else if (e.getSource() == answers[3])
        {
            checkAnswer(answers[3],list.get(QuestionNumber));
            showAnswer();
            QuestionNumber++;
            if(QuestionNumber<list.size()){
                try {


                    //reset();
                    printQuestion();
                    checkAnswer(answers[3],list.get(QuestionNumber));
                    revalidate();
                    repaint();
                    Thread.sleep(2000);
                    reset();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex)
                {
                    throw new RuntimeException(ex);
                }

            }else {
                QuestionNumber=0;
                reset();
                complete.doClick();
                numberOfCorrectAnswers=0;
            }

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
