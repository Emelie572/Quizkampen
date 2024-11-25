import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryGUI extends JPanel implements ActionListener
{
    JPanel panel = new JPanel(new GridLayout(4, 1));
    JButton[] buttons = new JButton[3];
    JLabel label = new JLabel("Choose one category");

    CategoryGUI()
    {
        panel.add(label);

        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new JButton("Category " + (i + 1));
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
            buttons[i].setBackground(Color.WHITE);
        }

        //setCategories();

        add(panel);
    }

     /* metod för att sätta kategorierna på knapparna
    public void setCategories(Quiz categories){
        for (int i = 0; i < categories.size(); i++){
            buttons[i].setText(categories[i]);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
            buttons[i].setBackground(Color.WHITE);
        }
    }*/

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
