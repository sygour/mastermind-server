package fr.sgo.mastermindserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.sgo.mastermindserver.game.Player;
import java.util.UUID;

@JsonSerialize
public class PlayerScore {
    @JsonProperty
    private final UUID id;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final int score;

    public PlayerScore(Player player, int score) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = score;
    }
}
