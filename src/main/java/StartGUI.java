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
    private final JLabel label = new JLabel("Enter Your Name:");
    private String name;

    StartGUI()
    {
        panel.add(label);
        panel.add(textField);
        panel.add(button);
        add(panel);
        button.setBorder(new LineBorder(Color.BLUE,1, true));
        button.setPreferredSize(new Dimension(80, 30));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        button.addActionListener(this);
        textField.addActionListener(this);
    }

    public void waitingForPlayerLable(Quiz inputQuiz) { //TODO Fixa så att den kommer upp när den ska.
        label.setText("Väntar på : " +
                inputQuiz.getPlayerChoosingCategory().replaceAll(
                        "[^a-zA-Z]","")+"...");
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
}
