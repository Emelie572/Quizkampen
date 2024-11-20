public class Protocol {

    private final int WAITING = 0;
    private final int ROUNDSCORE = 1;
    private final int FINISHED = 2;
    private int state = WAITING;

    private int rounds = 0;
    private int playerCheckerCount = 0;
    private Quiz outputQuiz = new Quiz();
    private ScoreTable scoreTable = new ScoreTable();

    //TODO set this.rounds from properties.
    public Protocol() {
        this.rounds = 2;
    }

    public synchronized Quiz proccesQuizInput(Quiz inputQuiz) {
        if (state == WAITING ) {
            if (playerCheckerCount == 1){
                playerCheckerCount++;
                state = ROUNDSCORE;
            }
            playerCheckerCount++;

        } else if (state == ROUNDSCORE) {
            String playerName = inputQuiz.playerName();
            int score = inputQuiz.getScore();
            scoreTable.updateScoreTable(playerName, score);
            inputQuiz.setScoreMessage(scoreTable);
            outputQuiz = inputQuiz;

            if (playerCheckerCount == 1){
                playerCheckerCount--;
                state = WAITING;
                if(rounds == 0){
                    state = FINISHED;
                }
                rounds--;
            }
            playerCheckerCount--;
        } else if (state == FINISHED) {
            outputQuiz = new Quiz();
            state = WAITING;
        }
        return outputQuiz;
    }

}
