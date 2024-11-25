import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ProtocolGUI
{
    final protected int START=0; //startsskärmen
    final protected int CHOOSE_CATEGORIES=1; //välja kategori
    final protected int IN_QUESTIONS_LOOP=2; //få x antal fågor
    final protected int RESULT=3; //resultat
    final protected int END=4; //slutskärm?

    StartGUI startGUI = new StartGUI();
    CategoryGUI categoryGUI = new CategoryGUI();
    QuestionGUI questionGUI = new QuestionGUI();
    int questionCount=0;
    int roundCount =0;

    protected int state = CHOOSE_CATEGORIES;

    public JPanel metod(List<List> list, JPanel bottomPanel) throws IOException
    {
        if(state == START){

            bottomPanel.add(startGUI);
            startGUI.setVisible(true);
            System.out.println("start");
            state=CHOOSE_CATEGORIES;
            return bottomPanel;
        }

        if (roundCount<3)
        {
            if (state == CHOOSE_CATEGORIES)
            {
                state = IN_QUESTIONS_LOOP;
                bottomPanel.removeAll();
                bottomPanel.add(categoryGUI);
                System.out.println("kategori");
                categoryGUI.setVisible(true);
                questionCount=0;
                return bottomPanel;
            }

            if (state == IN_QUESTIONS_LOOP)
            {

                if (questionCount < list.size())
                {
                    System.out.println("här då");
                    bottomPanel.removeAll();
                    bottomPanel.add(questionGUI);
                    questionGUI.printQuestion(list.get(questionCount));
                    questionGUI.reset();
                    questionGUI.setVisible(true);
                    questionCount++;
                }

                System.out.println("hej");
                if (questionCount == list.size())
                {
                    state = RESULT;
                }
            } else if (state == RESULT)
            {
                bottomPanel.removeAll();
                roundCount++;
                state = CHOOSE_CATEGORIES;
            }
        }
        return bottomPanel;

    }
}
