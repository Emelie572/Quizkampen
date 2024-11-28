import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGUI extends JPanel implements ActionListener
{
    private final JButton button = new JButton("Enter");
    private final JPanel panel = new JPanel(new GridLayout(3,1));
    private final JTextField textField = new JTextField(20);
    private final JLabel label = new JLabel("Enter Your Name:",SwingConstants.CENTER);
    private String name;
    private boolean haveOpponent = false;
    private final Color BLUE_COLOR = new Color(30, 70, 150);
    private final Color WHITE_COLOR = new Color(255, 255, 255);

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
        button.addActionListener(this);
        textField.addActionListener(this);
    }

    public void waitingForPlayerLable(String opponent) { //TODO Fixa så att den kommer upp när den ska.
        label.setText("Väntar på : " + opponent.replaceAll("[^a-zA-Z]","")+"...");
        haveOpponent = true;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        name = textField.getText();
        label.setText("Välkommen " + textField.getText()+"!");
        label.setFont((new Font("Arial", Font.PLAIN, 20)));
        button.setVisible(false);
        textField.setVisible(false);
    }

    public String getname(){
        return name;
    }

    public boolean HaveOpponent() {
        return haveOpponent;
    }
}
