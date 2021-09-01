package fr.sgo.mastermindserver.checker;

import java.util.List;

public interface Result {
    boolean isCorrect();
    List<State> getResult();

    enum State {
        CORRECT,
        INCORRECT,
        MISS_PLACED
    }
}
