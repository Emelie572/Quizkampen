import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryGUI extends JPanel
{
    JButton button = new JButton("knapp 4");
    CategoryGUI(){
        setBackground(Color.WHITE);
        add(new Label("kort 4"));

        add(button);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setBackground(Color.blue);
            }
        });
    }
    public void reset(){
        setBackground(Color.white);
    }
}
