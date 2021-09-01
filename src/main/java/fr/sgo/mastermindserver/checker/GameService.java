package fr.sgo.mastermindserver.checker;

import fr.sgo.mastermindserver.rest.Game;
import fr.sgo.mastermindserver.rest.PropositionResult;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final Map<UUID, Checker> games = new HashMap<>();
    private final RandomProvider randomProvider;

    public GameService(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public Game create() {
        int size = randomProvider.randomSize();
        List<Pawn> pawns = IntStream.range(0, size)
                .mapToObj(i -> randomProvider.randomPawn())
                .collect(Collectors.toList());
        UUID gameId = UUID.randomUUID();
        games.put(gameId, new Checker(pawns));
        return new Game(gameId, size, Arrays.stream(Pawn.values()).map(Enum::toString).collect(Collectors.toList()));
    }

    public PropositionResult check(UUID gameId, List<Pawn> pawns) {
        Checker checker = games.get(gameId);
        if (checker == null) {
            throw new MastermindException("unknown game id");
        }
        return new PropositionResult(checker.check(pawns));
    }
}
