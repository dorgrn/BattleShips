package com.BattleShipsWebApp.utils;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

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

    private static String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private String getAbsolutePathOfResource(String resource) {
        URL url = this.getClass().getResource(resource);
        return url != null ? url.getPath() : "?";
    }


//    private String getSubmittedFile(HttpServletRequest request) throws IOException, ServletException {
//        StringBuilder fileContent = new StringBuilder();
//
//        Collection<Part> parts = request.getParts();
//
//        for ( Part part : parts ) {
//            //to write the content of the file to an actual file in the system (will be created at c:\samplefile)
//            part.write("samplefile");
//
//            //to write the content of the file to a string
//            fileContent.append(readFromInputStream(part.getInputStream()));
//        }
//
//        return fileContent.toString();
//    }


    private String getResourceContent(String resource) {
        StringBuilder result = new StringBuilder();
        try (InputStream stream = this.getClass().getResourceAsStream(resource)) {
            Scanner scanner = new Scanner(stream, "UTF-8");
            while (scanner.hasNextLine()) {
                result.append(scanner.nextLine()).append("\n\r");
            }
        } catch (Exception exception) {
            return "Error: Failed to read file!";
        }
        return result.toString();
    }
}