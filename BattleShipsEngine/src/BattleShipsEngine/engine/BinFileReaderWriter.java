package BattleShipsEngine.engine;

import java.io.*;

public class BinFileReaderWriter {
    private static final String fileExtension = ".bin";

    private final static String path = System.getProperty("user.home");

    public void saveGameToFile(String fileName, String directoryName, Game game) throws IOException, ConfigException {
        ObjectOutputStream objectOutputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (!fileName.endsWith(fileExtension)) {
                fileName += fileExtension;
            }
            File directory = new File(path + File.separator + directoryName);

            if (directory.exists() || directory.mkdirs()){
                File savedFile = new File(path + File.separator + directoryName + File.separator + fileName);
                savedFile.createNewFile();
                fileOutputStream = new FileOutputStream(savedFile);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(game);
            }

        } catch (IOException e) {
            throw new ConfigException("Error saving to file. " + e.getMessage());
        } finally {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
        }
    }

    public Game loadGameFromFile(String directoryName, String fileName) throws IOException, ClassNotFoundException {
        Game game;
        ObjectInputStream objectInputStream = null;
        try {
            if (!fileName.endsWith(fileExtension)) {
                fileName += fileExtension;
            }
            FileInputStream streamIn = new FileInputStream(path + File.separator +  directoryName + File.separator + fileName);
            objectInputStream = new ObjectInputStream(streamIn);

            game = (Game) objectInputStream.readObject();
        } finally {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        }
        return game;
    }

}