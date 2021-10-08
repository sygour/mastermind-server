package fr.sgo.mastermindserver.game;

import fr.sgo.mastermindserver.checker.Checker;
import fr.sgo.mastermindserver.checker.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class GameResult {
    private final UUID gameId;
    private final Checker checker;
    private final List<Player> podium = new ArrayList<>();
    private final Map<Player, Integer> scores = new HashMap<>();

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
        if (podium.contains(player)) {
            return;
        }
        if (result.isCorrect()) {
            podium.add(player);
        }
        scores.put(player, getResult(player) + getCurrentResult(player));
    }

    private int getCurrentResult(Player player) {
        int position = podium.indexOf(player);
        switch (position) {
            case -1: return -1;
            case 0: return 5;
            case 1: return 3;
            case 2: return 2;
            default: return 1;
        }
    }

    public int getResult(Player player) {
        return scores.getOrDefault(player, 0);
    }
}
