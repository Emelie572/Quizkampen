import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGUI extends JPanel
{
    JButton button = new JButton("Enter");
    JPanel panel = new JPanel(new GridLayout(3,1));
    JTextField textField = new JTextField(20);
    JLabel label = new JLabel("Enter Your Name:");
    String name;
    private ActionListener nameListener;

    public String getname(){
        return name;
    }

    StartGUI()
    {
        panel.add(label);
        panel.add(textField);
        panel.add(button);
        add(panel);
        button.setBorder(new LineBorder(Color.BLUE,1, true));
        button.setPreferredSize(new Dimension(80, 30));
        label.setFont(new Font("Arial", Font.PLAIN, 15));

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                name = textField.getText();
                label.setText("Välkommen " + textField.getText()+"!"); //label.setText("Välkommen " + textField.getText() +". Väntar på motståndare...");
                label.setFont((new Font("Arial", Font.PLAIN, 20)));
                button.setVisible(false);
                textField.setVisible(false);
                getname();//test
                System.out.println(getname());//

                if (nameListener != null) {
                    nameListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Name Entered"));
                }
            }
        });
    }

    public void setNameListener(ActionListener listener){
        this.nameListener =  listener;
    }

    public void reset()
    {
        label.setText("Enter Your Name:");
        textField.setVisible(true);
        textField.setFont(getFont().deriveFont(Font.PLAIN));
        button.setVisible(true);
        textField.setText("");
    }
}
