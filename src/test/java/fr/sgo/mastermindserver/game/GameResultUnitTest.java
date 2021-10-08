package fr.sgo.mastermindserver.game;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.sgo.mastermindserver.checker.Checker;
import fr.sgo.mastermindserver.checker.Result;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GameResultUnitTest {
    @Test
    void shouldReturnZeroScoreByDefault() {
        GameResult gameResult = new GameResult(UUID.randomUUID(), mock(Checker.class));
        Player player = mock(Player.class);

        assertThat(gameResult.getResult(player)).isZero();
    }

    @Test
    void shouldBeNegativeOnFailure() {
        GameResult gameResult = new GameResult(UUID.randomUUID(), mock(Checker.class));
        Player player = mock(Player.class);
        Result failure = mock(Result.class);

        gameResult.setResult(player, failure);

        assertThat(gameResult.getResult(player)).isNegative();
    }

    @Test
    void shouldBePositiveOnSuccess() {
        GameResult gameResult = new GameResult(UUID.randomUUID(), mock(Checker.class));
        Player player = mock(Player.class);
        Result success = mock(Result.class);
        when(success.isCorrect()).thenReturn(true);

        gameResult.setResult(player, success);

        assertThat(gameResult.getResult(player)).isPositive();
    }

    @Test
    void shouldNotModifyScoreOnMultipleSuccess() {
        GameResult gameResult = new GameResult(UUID.randomUUID(), mock(Checker.class));
        Player player = mock(Player.class);
        Result success = mock(Result.class);
        when(success.isCorrect()).thenReturn(true);
        gameResult.setResult(player, success);
        int score = gameResult.getResult(player);

        gameResult.setResult(player, success);
        gameResult.setResult(player, success);
        gameResult.setResult(player, success);

        assertThat(gameResult.getResult(player)).isEqualTo(score);
    }

    @Test
    void shouldComputeScoreForMultiplePlayers() {
        GameResult gameResult = new GameResult(UUID.randomUUID(), mock(Checker.class));
        Player first = mock(Player.class);
        Player second = mock(Player.class);
        Player third = mock(Player.class);
        Player fourth = mock(Player.class);
        Result success = mock(Result.class);
        when(success.isCorrect()).thenReturn(true);
        Result failure = mock(Result.class);

        gameResult.setResult(first, failure);
        gameResult.setResult(second, failure);
        gameResult.setResult(second, failure);
        gameResult.setResult(first, success);
        gameResult.setResult(second, success);
        gameResult.setResult(third, failure);
        gameResult.setResult(third, failure);
        gameResult.setResult(third, failure);

        int firstScore = gameResult.getResult(first);
        int secondScore = gameResult.getResult(second);
        int thirdScore = gameResult.getResult(third);
        int fourthScore = gameResult.getResult(fourth);

        assertThat(firstScore).isEqualTo(4); // -1 + 5
        assertThat(secondScore).isEqualTo(1); // -1 + -1 + 3
        assertThat(thirdScore).isEqualTo(-3); // -1 + -1 + -1
        assertThat(fourthScore).isZero(); // 0
    }
}
