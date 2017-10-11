package BattleShipsEngine.engine;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigException extends Exception {

    private static final Logger LOGGER = Logger.getLogger(GameConfig.class.getName());

    public ConfigException(String message) {
        super(message);
        LOGGER.log(Level.FINER, message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
