package fr.sgo.mastermindserver.game;

import fr.sgo.mastermindserver.checker.MastermindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class GameMaster {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMaster.class);
    private static final int ONE_MINUTE = 60000;
    private final GameService gameService;

    GameMaster(GameService gameService) {
        this.gameService = gameService;
    }

    @Scheduled(fixedRate = ONE_MINUTE)
    public void createNewGame() {
        LOGGER.info("Creating new game");
        try {
            gameService.create();
        } catch (MastermindException exception) {
            LOGGER.warn(exception.getMessage());
        }
    }
}
