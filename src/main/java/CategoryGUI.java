import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoryGUI extends JPanel implements ActionListener
{
    JPanel panel = new JPanel(new GridLayout(4, 1));
    JButton[] buttons = new JButton[3];
    JLabel label = new JLabel("Choose one category");

    CategoryGUI()
    {
        panel.add(label);
        setMinimumSize(new Dimension(400, 300));

        label.setFont(new Font("Arial", Font.PLAIN, 15));
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new JButton("Category " + (i + 1));
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setPreferredSize(new Dimension(50, 40));
            buttons[i].setOpaque(true);
            buttons[i].setBorder(new LineBorder(Color.BLUE,1, true));
        }
        add(panel);
    }

    public void setCategories(List categories){
        for (int i = 0; i < categories.size(); i++){
            buttons[i].setText(categories.get(i).toString());
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
            buttons[i].setBackground(Color.WHITE);
        }
        reset(buttons);
    }

    public void reset(JButton[] categories)
    {
        label.setText("Choose one category");
        for (JButton b : categories)
        {
            b.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == buttons[0])
        {
            label.setText("you choosed " + buttons[0].getText());
        } else if (e.getSource() == buttons[1])
        {
            label.setText("you choosed " + buttons[1].getText());
        } else if (e.getSource() == buttons[2])
        {
            label.setText("you choosed " + buttons[2].getText());
        }
        buttons[0].setVisible(false);
        buttons[1].setVisible(false);
        buttons[2].setVisible(false);
    }
}
