package fr.sgo.mastermindserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;

@JsonDeserialize
public class PlayerScore {
    @JsonProperty
    private final UUID id;
    @JsonProperty
    private final int score;

    public PlayerScore(UUID id, int score) {
        this.id = id;
        this.score = score;
    }
}
