package fr.sgo.mastermindserver.game;

import fr.sgo.mastermindserver.checker.Checker;
import fr.sgo.mastermindserver.checker.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class GameResult {
    private final UUID gameId;
    private final Checker checker;
    private final List<Player> podium = new ArrayList<>();

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
        if (result.isCorrect() && !podium.contains(player)) {
            podium.add(player);
        }
    }

    public int getResult(Player player) {
        int position = podium.indexOf(player);
        switch (position) {
            case -1: return -1;
            case 0: return 5;
            case 1: return 3;
            case 2: return 2;
            default: return 0;
        }
    }
}
