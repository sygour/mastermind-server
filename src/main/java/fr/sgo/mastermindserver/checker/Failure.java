package fr.sgo.mastermindserver.checker;

import java.util.List;
import java.util.stream.Collectors;

class Failure implements Result {
    private final List<State> result;

    public Failure(List<Pair> pairs) {
        result = pairs.stream().map(Pair::getState).collect(Collectors.toList());
    }

    @Override
    public boolean isCorrect() {
        return false;
    }

    @Override
    public List<State> getResult() {
        return result;
    }
}
