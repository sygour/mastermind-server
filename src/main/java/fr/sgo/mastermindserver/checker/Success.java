package fr.sgo.mastermindserver.checker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Success implements Result {
    private final List<State> result;

    public Success(int size) {
        result = IntStream.range(0, size).mapToObj(i -> State.CORRECT).collect(Collectors.toList());
    }

    @Override
    public List<State> getResult() {
        return result;
    }

    @Override
    public boolean isCorrect() {
        return true;
    }
}
