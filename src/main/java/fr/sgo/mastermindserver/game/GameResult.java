package fr.sgo.mastermindserver.game;

import fr.sgo.mastermindserver.checker.Checker;
import fr.sgo.mastermindserver.checker.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class GameResult {
    private final UUID gameId;
    private final Checker checker;
    private final Map<Player, Result> results = new HashMap<>();

    GameResult(UUID gameId, Checker checker) {
        this.gameId = gameId;
        this.checker = checker;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setResult(Player player, Result result) {
        results.put(player, result);
    }

    public Optional<Result> getResult(Player player) {
        return Optional.ofNullable(results.get(player));
    }
}
