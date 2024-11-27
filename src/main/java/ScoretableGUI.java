
import javax.swing.*;
import java.awt.*;

public class ScoretableGUI extends JFrame {

    private JPanel contentPanel;

    public ScoretableGUI(String player1, String player2, int rounds) {

        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        addHeader(player1,player2); //någon metod som skapar header

        for (int i = 0; i < rounds; i++) {
            for(int i = 1; i <=rounds; i++) {
                addRounds(); //någon metod som lägger till rader för varje rond, och typ har en "dolt" text innan

            }

        JLabel scoreResultTitel = new JLabel();
        scoreResultTitel.setFont(new Font("Arial", Font.BOLD, 15));
        add(scoreResultTitel, BorderLayout.NORTH);

        }

        //Lägg till header metod();

        //Lägg till rundor metod();

        //Lägg till en tabell eller celler metod();

        //Metod för att uppdatera poängen

}
