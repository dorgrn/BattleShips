package com.BattleShipsWebApp.utils;

import java.io.*;

public class InputFileUtils {
    private final static String USER_PATH = System.getProperty("user.home");

    public static File inputStreamToFile(final InputStream inputStream, final String directoryName, String fileName, final String fileExtension) {
        OutputStream outputStream = null;
        File savedFile = null;

        try {
            if (!fileName.endsWith(fileExtension)) {
                fileName += '.' + fileExtension;
            }
            File directory = new File(USER_PATH + File.separator + directoryName);

            if (directory.exists() || directory.mkdirs()){
                savedFile = new File(USER_PATH + File.separator + directoryName + File.separator + fileName);
                savedFile.createNewFile();

                outputStream =
                        new FileOutputStream(savedFile);

                readResource(inputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams(inputStream, outputStream);

        }

        return savedFile;
    }

    private static void readResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
    }

    private static void closeStreams(InputStream inputStream, OutputStream outputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                // outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}