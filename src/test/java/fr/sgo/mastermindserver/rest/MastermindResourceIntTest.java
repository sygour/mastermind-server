package fr.sgo.mastermindserver.rest;

import static org.mockito.Mockito.*;

import fr.sgo.mastermindserver.game.GameService;
import fr.sgo.mastermindserver.game.Player;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@Import(MastermindResource.class)
class MastermindResourceIntTest {
    @MockBean
    private GameService gameService;
    @Autowired
    private WebTestClient webClient;

    @Test
    void shouldNotGetGame() {
        webClient.get().uri("/mastermind")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldGetGame() {
        when(gameService.getCurrentGame()).thenReturn(mock(Game.class));

        webClient.get().uri("/mastermind")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldRegisterPlayer() {
        UUID id = UUID.randomUUID();
        when(gameService.registerPlayer("coucou")).thenReturn(new Player(id, "coucou"));

        webClient.post().uri("/mastermind/players?name=coucou")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[{\"id\":\"" + id +"\",\"name\":\"coucou\"}]");
    }

    @Test
    void shouldPostSolution() {
        when(gameService.check(any(), any(), any())).thenReturn(mock(PropositionResult.class));

        webClient.post().uri("/mastermind/{id}/solutions", UUID.randomUUID())
                .body(BodyInserters.fromValue(List.of()))
                .header("player", UUID.randomUUID().toString())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldGetScores() {
        UUID id = UUID.randomUUID();
        when(gameService.getScores()).thenReturn(List.of(new PlayerScore(new Player(id, "coucou"), 12)));

        webClient.get().uri("/mastermind/players")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[{\"id\":\"" + id +"\",\"name\":\"coucou\",\"score\":12}]");
    }
}
