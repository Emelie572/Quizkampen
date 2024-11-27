package Database;

import java.io.Serializable;

public class TriviaCategory implements Serializable {
    private int id;
    private String name;
    private boolean used = false;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}