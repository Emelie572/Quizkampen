import Database.TriviaCategory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoryGUI extends JPanel implements ActionListener
{
    private final JPanel panel = new JPanel(new GridLayout(4, 1));
    private final JButton[] buttons = new JButton[3];
    private final JLabel label = new JLabel("Choose one category", JLabel.CENTER);
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final Color WHITE_COLOR = new Color(255, 255, 255);

    CategoryGUI(ActionListener listener)
    {
        setBackground(BLUE_COLOR);

        panel.setBackground(BLUE_COLOR);
        panel.setOpaque(true);

        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(WHITE_COLOR);
        label.setOpaque(false);

        panel.add(label);
        panel.setSize(new Dimension(400,300));
        setMinimumSize(new Dimension(600, 400));

        label.setFont(new Font("Arial", Font.PLAIN, 15));
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new JButton();
            buttons[i].addActionListener(listener);
            buttons[i].setSize(new Dimension(300, 120));
            buttons[i].setOpaque(true);
            buttons[i].setBackground(WHITE_COLOR);
            buttons[i].setForeground(BLUE_COLOR);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            buttons[i].setBorder(new LineBorder(BLUE_COLOR,1, true));
            panel.add(buttons[i]);

        }
        add(panel);
    }

    public void setCategories(List <TriviaCategory> categories){

        for (int i = 0; i < categories.size(); i++){
            buttons[i].setText(categories.get(i).getName());
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }
        reset(buttons);
    }

    public void reset(JButton[] categories)
    {
        label.setText("Choose one category");
        for (JButton b : categories)
        {
            b.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == buttons[0])
        {
           label.setText("väntar på motståndare");
           //label.setText("you choosed " + buttons[0].getText());
        } else if (e.getSource() == buttons[1])
        {
            label.setText("väntar på motståndare");
            //label.setText("you choosed " + buttons[1].getText());
        } else if (e.getSource() == buttons[2])
        {
           label.setText("väntar på motståndare");
            //label.setText("you choosed " + buttons[2].getText());
        }
        buttons[0].setEnabled(false);
        buttons[1].setEnabled(false);
        buttons[2].setEnabled(false);
    }
}
