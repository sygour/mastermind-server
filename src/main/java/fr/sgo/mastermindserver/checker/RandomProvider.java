package fr.sgo.mastermindserver.checker;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
class RandomProvider {
    private final Random random;

    RandomProvider() {
        this.random = new Random();
    }

    public int randomSize() {
        return 5 + random.nextInt(5);
    }

    public Pawn randomPawn() {
        Pawn[] values = Pawn.values();
        int length = values.length;
        return values[random.nextInt(length)];
    }
}
