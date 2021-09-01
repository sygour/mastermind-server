package fr.sgo.mastermindserver.rest;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameIntTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerialize() throws JsonProcessingException {
        Game game = new Game(UUID.fromString("cd2f6349-07a1-4f33-85d8-e7e5b0be70bd"), 10, List.of("RED"));

        String json = objectMapper.writeValueAsString(game);

        assertThat(json).isEqualTo("{\"id\":\"cd2f6349-07a1-4f33-85d8-e7e5b0be70bd\",\"size\":10,\"colors\":[\"RED\"]}");
    }
}
