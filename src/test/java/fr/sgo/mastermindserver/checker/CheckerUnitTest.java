package fr.sgo.mastermindserver.checker;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CheckerUnitTest {
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenWrongInput(List<Pawn> pawns) {
        Checker checker = new Checker(Pawn.RED, Pawn.RED, Pawn.RED, Pawn.RED, Pawn.RED);

        assertThatThrownBy(() -> checker.check(pawns)).isExactlyInstanceOf(MastermindException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 6, 10})
    void shouldThrowExceptionWhenNumberOfPawns(int size) {
        Checker checker = new Checker(Pawn.RED, Pawn.RED, Pawn.RED, Pawn.RED, Pawn.RED);
        List<Pawn> pawns = IntStream.range(0, size).mapToObj(index -> Pawn.RED).collect(Collectors.toList());

        assertThatThrownBy(() -> checker.check(pawns)).isExactlyInstanceOf(MastermindException.class);
    }

    @ParameterizedTest
    @EnumSource(value = Pawn.class, mode = EnumSource.Mode.EXCLUDE, names = "RED")
    void shouldCheckWrongSingleCombination(Pawn pawn) {
        Checker checker = new Checker(Pawn.RED);
        assertThat(checker.check(List.of(pawn))).matches(Predicate.not(Result::isCorrect));
    }

    @ParameterizedTest
    @EnumSource(value = Pawn.class)
    void shouldCheckCorrectSingleCombination(Pawn pawn) {
        Checker checker = new Checker(pawn);
        assertThat(checker.check(List.of(pawn))).matches(Result::isCorrect);
    }

    @Test
    void shouldCheckWrongCombination() {
        Checker checker = new Checker(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.ORANGE);
        assertThat(checker.check(List.of(Pawn.BLUE, Pawn.BLUE, Pawn.GREEN, Pawn.ORANGE))).matches(Predicate.not(Result::isCorrect));
        assertThat(checker.check(List.of(Pawn.RED, Pawn.BLACK, Pawn.GREEN, Pawn.ORANGE))).matches(Predicate.not(Result::isCorrect));
        assertThat(checker.check(List.of(Pawn.RED, Pawn.BLUE, Pawn.WHITE, Pawn.ORANGE))).matches(Predicate.not(Result::isCorrect));
        assertThat(checker.check(List.of(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.YELLOW))).matches(Predicate.not(Result::isCorrect));
    }

    @Test
    void shouldCheckCorrectCombination() {
        Checker checker = new Checker(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.ORANGE);
        assertThat(checker.check(List.of(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.ORANGE))).matches(Result::isCorrect);
    }

    @Test
    void shouldReturnResultStateWhenIncorrect() {
        Checker checker = new Checker(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.ORANGE);

        assertThat(checker.check(List.of(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.BLACK)))
                .isInstanceOf(Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(Failure.class))
                .extracting(Failure::getResult)
                .isEqualTo(List.of(Result.State.CORRECT, Result.State.CORRECT, Result.State.CORRECT, Result.State.INCORRECT));
    }

    @Test
    void shouldReturnResultStateWhenMissPlaced() {
        Checker checker = new Checker(Pawn.RED, Pawn.BLUE, Pawn.GREEN, Pawn.ORANGE);

        assertThat(checker.check(List.of(Pawn.RED, Pawn.RED, Pawn.BLUE, Pawn.RED)))
                .isInstanceOf(Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(Failure.class))
                .extracting(Failure::getResult)
                .isEqualTo(List.of(Result.State.CORRECT, Result.State.INCORRECT, Result.State.MISS_PLACED, Result.State.INCORRECT));
    }
}
