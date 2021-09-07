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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final Map<UUID, GameResult> games = new HashMap<>();
    private final Set<Player> players = new HashSet<>();
    private final RandomProvider randomProvider;

    private GameResult currentGame;
    private int playerCount;

    public GameService(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    private void assertIsSetUp() {
        if (playerCount <= 0) {
            throw new MastermindException("game is not setup");
        }
    }

    private void assertIsReady() {
        assertIsSetUp();
        if (players.size() < playerCount) {
            throw new MastermindException("game is not ready");
        }
    }

    public Player registerPlayer(String name) {
        assertIsSetUp();
        if (players.stream().map(Player::getName).anyMatch(Predicate.isEqual(name))) {
            throw new MastermindException("player already exists: " + name);
        }
        if (players.size() >= playerCount) {
            throw new MastermindException("maximum number of players reached");
        }
        Player player = new Player(UUID.randomUUID(), name);
        players.add(player);
        return player;
    }

    void create() {
        assertIsReady();
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
        assertIsSetUp();
        if (currentGame == null) {
            return null;
        }
        return new Game(currentGame.getGameId(), currentGame.getChecker().getSize(), Arrays.stream(Pawn.values()).map(Enum::toString).collect(Collectors.toList()));
    }

    public PropositionResult check(UUID playerId, UUID gameId, List<Pawn> pawns) {
        assertIsSetUp();
        Player player = players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new MastermindException("unknown player id"));
        if (!currentGame.getGameId().equals(gameId)) {
            throw new MastermindException("unknown game id, current game id: " + currentGame.getGameId());
        }
        Checker checker = currentGame.getChecker();
        Result result = checker.check(pawns);
        currentGame.setResult(player, result);
        return new PropositionResult(result);
    }

    public List<PlayerScore> getScores() {
        assertIsSetUp();
        ArrayList<PlayerScore> playerScores = new ArrayList<>();
        Collection<GameResult> gameResults = games.values();
        players.forEach(player -> {
            int score = gameResults.stream()
                    .mapToInt(gameResult -> gameResult.getResult(player))
                    .sum();
            playerScores.add(new PlayerScore(player, score));
        });
        return playerScores;
    }

    public void setup(int playerCount) {
        this.playerCount = playerCount;
    }
}
