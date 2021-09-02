package fr.sgo.mastermindserver.game;

import fr.sgo.mastermindserver.checker.Checker;
import fr.sgo.mastermindserver.checker.MastermindException;
import fr.sgo.mastermindserver.checker.Pawn;
import fr.sgo.mastermindserver.checker.Result;
import fr.sgo.mastermindserver.rest.Game;
import fr.sgo.mastermindserver.rest.PlayerScore;
import fr.sgo.mastermindserver.rest.PropositionResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final Map<UUID, GameResult> games = new HashMap<>();
    private final Set<Player> players = new HashSet<>();
    private final RandomProvider randomProvider;

    private GameResult currentGame;

    public GameService(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public Player registerPlayer() {
        Player player = new Player(UUID.randomUUID());
        players.add(player);
        return player;
    }

    void create() {
        storeCurrentGame();
        createNewGame();
    }

    private void storeCurrentGame() {
        if (currentGame == null) {
            return;
        }
        games.put(currentGame.getGameId(), currentGame);
    }

    private void createNewGame() {
        int size = randomProvider.randomSize();
        List<Pawn> pawns = IntStream.range(0, size)
                .mapToObj(i -> randomProvider.randomPawn())
                .collect(Collectors.toList());
        UUID gameId = UUID.randomUUID();
        currentGame = new GameResult(gameId, new Checker(pawns));
    }

    public Game getCurrentGame() {
        if (currentGame == null) {
            return null;
        }
        return new Game(currentGame.getGameId(), currentGame.getChecker().getSize(), Arrays.stream(Pawn.values()).map(Enum::toString).collect(Collectors.toList()));
    }

    public PropositionResult check(Player player, UUID gameId, List<Pawn> pawns) {
        if (!players.contains(player)) {
            throw new MastermindException("unknown player id");
        }
        if (!currentGame.getGameId().equals(gameId)) {
            throw new MastermindException("unknown game id, current game id: " + currentGame.getGameId());
        }
        Checker checker = currentGame.getChecker();
        Result result = checker.check(pawns);
        currentGame.setResult(player, result);
        return new PropositionResult(result);
    }

    public List<PlayerScore> getScores() {
        ArrayList<PlayerScore> playerScores = new ArrayList<>();
        Collection<GameResult> gameResults = games.values();
        players.forEach(player -> {
            int score = gameResults.stream().map(gameResult -> gameResult.getResult(player))
                    .mapToInt(result -> result.filter(Result::isCorrect).map(r -> 10).orElse(-1))
                    .sum();
            playerScores.add(new PlayerScore(player.getId(), score));
        });
        return playerScores;
    }
}
