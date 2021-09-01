package fr.sgo.mastermindserver.rest;

import static org.mockito.Mockito.*;

import fr.sgo.mastermindserver.checker.GameService;
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
    void shouldGetGame() {
        webClient.get().uri("/mastermind")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldPostSolution() {
        when(gameService.check(any(), any())).thenReturn(mock(PropositionResult.class));

        webClient.post().uri("/mastermind/{id}/solutions", UUID.randomUUID())
                .body(BodyInserters.fromValue(List.of()))
                .exchange()
                .expectStatus().isOk();
    }
}
