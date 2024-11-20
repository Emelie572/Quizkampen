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


    public Quiz() {
    }

    public void nästaFråga() {

    }

    public void svar() {

    }

    public void resultat() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
