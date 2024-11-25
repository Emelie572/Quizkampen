package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizSource implements Serializable {
    public int response_code;
    public List<Result> results;

}