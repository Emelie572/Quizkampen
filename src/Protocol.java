public class Protocol {

    private final int SENDINGQUIZ = 0;
    private final int ROUNDSCORE = 1;
    private int state = SENDINGQUIZ;

    private int rounds = 0;
    private int playerStateCount = 0;
    private Quiz outputQuiz;
    private ScoreTable scoreTable = new ScoreTable();

    //TODO set this.rounds from properties.
    public Protocol() {
        this.rounds = 2;
    }
//TODO Separat metod för playerStateScore hantering.
    public synchronized Quiz proccesQuizInput(Quiz inputQuiz)  {
        if (state == SENDINGQUIZ) {
            if (playerStateCount == 0) {
                outputQuiz = new Quiz();
                playerStateCount++;
            }else if (playerStateCount == 1){
                playerStateCount++;
                state = ROUNDSCORE;
            }
//TODO Definera dataflöde. Ändra så att setScoreMessage skickas när båda spelare har
// uppdaterat scoreTable.
        } else if (state == ROUNDSCORE) {
            if (playerStateCount == 2) {
                scoreTable.updateScoreTable(inputQuiz.playerName(),inputQuiz.getScore());
                playerStateCount--;
                return null;
            }else if (playerStateCount == 1) {
                scoreTable.updateScoreTable(inputQuiz.playerName(),inputQuiz.getScore());
                inputQuiz.setScoreMessage(scoreTable);
                inputQuiz.readOnly(true);
                outputQuiz = inputQuiz;
                playerStateCount--;
                state = SENDINGQUIZ;
            }
            //TODO lägg till logik för antal rundor.
        }
        return outputQuiz;
    }

}
