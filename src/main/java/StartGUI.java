import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGUI extends JPanel
{
    JButton button = new JButton("knapp 3");

    StartGUI()
    {
        add(new Label("kort 3"));

        add(button);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setBackground(Color.green);
            }
        });
    }

    public void reset()
    {
        setBackground(Color.white);
    }
}
