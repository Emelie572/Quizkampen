import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame
{
    private JPanel cardPanel;
    private CardLayout cardLayout;

    GUI()
    {
        cardPanel = new JPanel(new CardLayout());

        QuestionGUI questionGUI = new QuestionGUI();
        QuestionGUI questionGUI2 = new QuestionGUI();
        StartGUI startGUI = new StartGUI();
        CategoryGUI categoryGUI = new CategoryGUI();


        cardPanel.add(questionGUI, "card2");
        cardPanel.add(startGUI, "card3");
        cardPanel.add(questionGUI2, "card2");
        cardPanel.add(categoryGUI, "card4");

        JPanel buttonSpace = new JPanel();
        JButton play = new JButton("Play");

        cardLayout = (CardLayout) cardPanel.getLayout();

        play.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (questionGUI.isVisible())
                {
                    startGUI.reset();
                    play.setText("forsätt");
                } else if (startGUI.isVisible())
                {
                    play.setText("play");
                    questionGUI2.reset();
                } else if (questionGUI2.isVisible())
                {
                    play.setText("play");
                    categoryGUI.reset();
                } else if (categoryGUI.isVisible())
                {
                    questionGUI.reset();
                }
                cardLayout.next(cardPanel);
            }
        });

        buttonSpace.add(play);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(buttonSpace, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setSize(300, 400);
    }

    public static void main(String[] args)
    {
        GUI gui = new GUI();
    }
}
