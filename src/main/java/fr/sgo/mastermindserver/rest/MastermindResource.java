package fr.sgo.mastermindserver.rest;

import fr.sgo.mastermindserver.checker.MastermindException;
import fr.sgo.mastermindserver.checker.Pawn;
import fr.sgo.mastermindserver.game.GameService;
import fr.sgo.mastermindserver.game.Player;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mastermind")
class MastermindResource {
    private final GameService gameService;

    MastermindResource(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/players")
    public ResponseEntity<List<Player>> register(@RequestParam("name") String name) {
        return ResponseEntity.ok(List.of(gameService.registerPlayer(name)));
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerScore>> getScores() {
        return ResponseEntity.ok(gameService.getScores());
    }

    @PostMapping
    public ResponseEntity<Void> setup(@RequestParam("players") int playerCount) {
        gameService.setup(playerCount);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<Game> getGame() {
        Game currentGame = gameService.getCurrentGame();
        if (currentGame == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currentGame);
    }

    @PostMapping("{id}/solutions")
    public ResponseEntity<List<PropositionResult>> postProposition(@RequestHeader("player") UUID playerId, @PathVariable("id") UUID gameId, @RequestBody List<String> colors) {
        try {
            List<Pawn> pawns = colors.stream().map(Pawn::valueOf).collect(Collectors.toList());
            return ResponseEntity.ok(List.of(gameService.check(playerId, gameId, pawns)));
        } catch (MastermindException error) {
            return ResponseEntity.badRequest().header("error", error.getMessage()).build();
        }
    }
}
