import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Quiz implements ActionListener {

    String[] frågor = {"Vilken färg är solen?", "Är äpple en frukt eller en grönsak?"};

    String[][] alternativ = {{"Gul", "Rosa"}, {"Grönsak", "Frukt"}};

    char[] svar = {'A', 'B'};


    char gissa;
    char fråga;
    int index;
    int korrektSvar;
    int totaltAntalFrågor = frågor.length;
    int result;

    JFrame jf = new JFrame();
    JTextField jt = new JTextField();
    JTextArea jta = new JTextArea();
    JButton jbA = new JButton();
    JButton jbB = new JButton();
    JLabel jl_svarA = new JLabel();
    JLabel jl_svarB = new JLabel();


    public Quiz() {
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setSize(700, 700);
        jf.setVisible(true);
    }

    public void nästaFråga() {

    }

    public void svar() {

    }

    public void resultat() {

    }

    public static void main(String[] args) {
        Quiz quiz = new Quiz();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
