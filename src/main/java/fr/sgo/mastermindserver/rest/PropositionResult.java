package fr.sgo.mastermindserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.sgo.mastermindserver.checker.Result;
import java.io.Serializable;
import java.util.List;

@JsonSerialize
public class PropositionResult implements Serializable {
    @JsonProperty
    private final List<Result.State> result;

    public PropositionResult(Result result) {
        this.result = result.getResult();
    }
}
