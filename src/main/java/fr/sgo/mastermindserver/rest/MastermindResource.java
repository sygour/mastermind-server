package fr.sgo.mastermindserver.rest;

import fr.sgo.mastermindserver.checker.GameService;
import fr.sgo.mastermindserver.checker.MastermindException;
import fr.sgo.mastermindserver.checker.Pawn;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mastermind")
class MastermindResource {
    private final GameService gameService;

    MastermindResource(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<Game> getGame() {
        return ResponseEntity.ok(gameService.create());
    }

    @PostMapping("{id}/solutions")
    public ResponseEntity<List<PropositionResult>> postProposition(@PathVariable("id") UUID gameId, @RequestBody List<String> colors) {
        try {
            List<Pawn> pawns = colors.stream().map(Pawn::valueOf).collect(Collectors.toList());
            return ResponseEntity.ok(List.of(gameService.check(gameId, pawns)));
        } catch (MastermindException error) {
            return ResponseEntity.badRequest().header("error", error.getMessage()).build();
        }
    }
}
