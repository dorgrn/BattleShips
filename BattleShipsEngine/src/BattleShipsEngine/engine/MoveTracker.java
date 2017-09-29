package BattleShipsEngine.engine;

import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoveTracker {
    private final static String FILE_NAME_START = "turn_";
    private final static String DIR_NAME = "Battleship" + File.separator + "Saves";
    private final List<String> gameStatusList;
    private final List<String> fileNames;
    private BinFileReaderWriter readerWriter;
    private int iterator = 0;
    private SimpleIntegerProperty iteratorProp = new SimpleIntegerProperty(iterator);

    public MoveTracker() {
        gameStatusList = new ArrayList<>();
        fileNames = new ArrayList<>();
        readerWriter = new BinFileReaderWriter();
    }

    public void saveMove(Game game, String status) throws IOException, ConfigException {
        String currentFileName = FILE_NAME_START + (fileNames.size() + 1);
        fileNames.add(currentFileName);
        gameStatusList.add(status);
        iterator++;
        readerWriter.saveGameToFile(currentFileName, DIR_NAME, game);
    }

    public Game loadPreviousMove() throws IOException, ClassNotFoundException {
        if (iterator == 0) {
            return null;
        }
        iterator--;
        return readerWriter.loadGameFromFile(DIR_NAME, FILE_NAME_START + iterator);
    }

    public Game loadNextMove() throws IOException, ClassNotFoundException {
        if (iterator >= fileNames.size()) {
            return null;
        }
        iterator++;
        return readerWriter.loadGameFromFile(DIR_NAME, FILE_NAME_START + iterator);
    }

    public int getIterator() {
        return iterator;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public int getIteratorProp() {
        return iteratorProp.get();
    }

    public SimpleIntegerProperty iteratorPropProperty() {
        return iteratorProp;
    }

    public String getStatusAt(int iterator) {
        return gameStatusList.get(iterator);
    }
}
