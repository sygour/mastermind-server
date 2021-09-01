package fr.sgo.mastermindserver.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Checker {
    private final List<Pawn> solution;

    public Checker(Pawn pawn, Pawn... pawns) {
        this(Stream.concat(Stream.of(pawn), Arrays.stream(pawns)).collect(Collectors.toList()));
    }

    public Checker(List<Pawn> pawns) {
        solution = List.copyOf(pawns);
    }

    public Result check(List<Pawn> pawns) {
        assertInput(pawns);

        List<Pair> pairs = toPairs(solution, pawns);
        if (pairs.stream().allMatch(Pair::isCorrect)) {
            return new Success(solution.size());
        }

        List<Pawn> remainingPawns = new ArrayList<>(solution);
        pairs.stream()
                .filter(Pair::isCorrect)
                .map(Pair::getProposition)
                .forEach(remainingPawns::remove);
        pairs.stream()
                .filter(Predicate.not(Pair::isCorrect))
                .forEach(pair -> {
                    if (remainingPawns.remove(pair.getProposition())) {
                        pair.missPlaced();
                    }
                });
        return new Failure(pairs);
    }

    private List<Pair> toPairs(List<Pawn> solution, List<Pawn> pawns) {
        ArrayList<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < solution.size(); i++) {
            pairs.add(new Pair(solution.get(i), pawns.get(i)));
        }
        return pairs;
    }

    private void assertInput(Collection<Pawn> pawns) {
        if (pawns == null) {
            throw new MastermindException("null array");
        }
        if (pawns.isEmpty()) {
            throw new MastermindException("empty array");
        }
        if (pawns.size() != solution.size()) {
            throw new MastermindException("wrong number of pawns");
        }
    }
}
