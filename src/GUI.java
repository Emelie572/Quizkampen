import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI
{
    private JPanel cardPanel;
    private CardLayout cardLayout;

    GUI()
    {
        cardPanel = new JPanel(new CardLayout());

        GUI8_2 gui2 = new GUI8_2();
        GUI8_2 gui2_2 = new GUI8_2();
        GUI8_3 gui3 = new GUI8_3();
        GUI8_4 gui4 = new GUI8_4();


        cardPanel.add(gui2, "card2");
        cardPanel.add(gui3, "card3");
        cardPanel.add(gui2_2, "card2");
        cardPanel.add(gui4, "card4");

        JPanel buttonSpace = new JPanel();
        JButton play = new JButton("Play");

        cardLayout = (CardLayout) cardPanel.getLayout();

        play.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(gui2.isVisible()){
                    gui3.reset();
                    play.setText("forsätt");
                }else if(gui3.isVisible()){
                    play.setText("play");
                    gui2_2.reset();
                }else if(gui2_2.isVisible()){
                    play.setText("play");
                    gui4.reset();
                }
                else if(gui4.isVisible()){
                    gui2.reset();
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
        setSize(300,400);
    }

    public static void main(String[] args)
    {
        GUI gui = new GUI();
    }
}
