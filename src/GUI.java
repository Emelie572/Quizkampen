import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame implements ActionListener
{
    private JPanel bottompanel = new JPanel();

    ProtocolGUI protocolGUI = new ProtocolGUI();

    JPanel buttonSpace = new JPanel();
    JButton play = new JButton("Play");
    JButton continueButton = new JButton("Continue");
    int currentQuestion = 0;

    java.util.List<String> list = new ArrayList<>(java.util.List.of("Vilken färg är gräs? ", "rosa", "grön", "blå", "röd", "grön"));
    java.util.List<String> list2 = new ArrayList<>(java.util.List.of("Hur många dagar på 1 vecka? ", "3", "5", "7", "9", "7"));
    java.util.List<String> list3 = new ArrayList<>(java.util.List.of("Hur många bokstäver är det i alfabetet? ", "22", "25", "27", "29", "29"));
    java.util.List<java.util.List> questionsList = new ArrayList<>(List.of(list, list2, list3));

    GUI() throws IOException
    {
        play.addActionListener(this);
        continueButton.addActionListener(this);

        setTitle("Quizkampen");

        buttonSpace.add(play);
        buttonSpace.add(continueButton);

        continueButton.setVisible(false);

        setLayout(new BorderLayout());

        add(buttonSpace, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        bottompanel.add(protocolGUI.startGUI);
        setSize(300, 400);
        add(bottompanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == play)
        {
            try
            {
                protocolGUI.metod(questionsList, bottompanel);
                revalidate();
                repaint();

            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            System.out.println("här");
        }
    }

    public static void main(String[] args) throws IOException
    {
        GUI gui = new GUI();
    }
}
