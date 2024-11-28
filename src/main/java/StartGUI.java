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
    JLabel label = new JLabel("Enter Your Name:",SwingConstants.CENTER);
    String name;
    private ActionListener nameListener;
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final Color WHITE_COLOR = new Color(255, 255, 255);

    public String getname(){
        return name;
    }

    StartGUI()
    {
        setBackground(BLUE_COLOR);
        panel.setBackground(BLUE_COLOR);
        label.setForeground(WHITE_COLOR);

        panel.add(label);
        panel.add(textField);
        panel.add(button);
        add(panel);
        button.setBorder(new LineBorder(WHITE_COLOR,1, true));
        button.setPreferredSize(new Dimension(80, 30));
        label.setFont(new Font("Arial", Font.PLAIN, 15));


        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                name = textField.getText();
                label.setText("Välkommen " + textField.getText() +"\n"+"Väntar på motståndare..."); //label.setText("Välkommen " + textField.getText());
                label.setFont((new Font("Arial", Font.PLAIN, 20)));
                button.setVisible(false);
                button.setForeground(WHITE_COLOR);
                button.setBackground(BLUE_COLOR);
                textField.setVisible(false);
                getname();

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
        textField.setForeground(WHITE_COLOR);
        button.setVisible(true);
        textField.setText("");
    }
}
