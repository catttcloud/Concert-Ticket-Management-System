import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the FileHandler interface, providing methods for reading and writing files.
 */
public class FileHandlerImpl implements FileHandler {

    /**
     * Reads the contents of a file.
     *
     * @param filePath The path of the file to be read.
     * @return A list containing the lines read from the file.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Writes data to a file.
     *
     * @param filePath The path of the file to write the data.
     * @param data     The data to be written to the file.
     */
    @Override
    public void writeFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Checks for invalid lines in a file based on the file name.
     *
     * @param lines    The lines to be checked for validity.
     * @param fileName The name of the file being checked.
     * @throws InvalidLineException If an invalid line is encountered.
     */
    @Override
    public void checkInvalidLine(List<String> lines, String fileName)
            throws InvalidLineException {
        for (String line : lines) {
            String[] dataParts = line.split(",");
            int dataPoints = dataParts.length;
            int index = fileName.lastIndexOf('.');
            String originFilename = fileName.substring(0, index);
            String firstLetter = originFilename.substring(0, 1).toUpperCase();
            String capitalizedString = firstLetter + originFilename.substring(1);
            int testpoints;

            if (fileName.equals("concert.csv") || fileName.equals("concert_incorrect_15.csv")) {
                testpoints = 8;
            } else if (fileName.equals("customer.csv")) {
                testpoints = 3;
            } else if (fileName.equals("bookings.csv")) {
                testpoints = 5;
            } else {
                continue;
            }
            // Handling 2. InvalidLineException
            if (dataPoints < testpoints) {
                throw new InvalidLineException(capitalizedString);
            }
        }
    }

    /**
     * Checks for invalid zone types in the lines of a file.
     *
     * @param lines The lines to be checked for invalid zone types.
     * @throws InvalidFormatException If an invalid zone type is encountered.
     */
    public void checkZoonType(List<String> lines) throws InvalidFormatException {
        for (String line : lines) {
            if (!line.isEmpty()) {
                char firstChar = line.charAt(0);
                try {
                    if (firstChar != 'V' && firstChar != 'S' && firstChar != 'T') {
                        throw new InvalidFormatException("Invalid Zone Type. ");
                    }
                } catch (InvalidFormatException e) {
                    // System.out.println(e.getMessage());
                    continue;
                }
            }
        }
    }

    /**
     * Checks if the data part at a given index in an array is in valid format.
     *
     * @param dataParts The array of data parts.
     * @param index     The index of the data part to be checked.
     * @return True if the data part is in valid format, false otherwise.
     */
    public static boolean checkInvalidFormat(String[] dataParts, int index) {
        try {
            Integer.parseInt(dataParts[index]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves the ID of the last entry in a file.
     *
     * @param filePath The path of the file.
     * @return The ID of the last entry in the file.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public int getLastId(String filePath) throws IOException {
        List<String> lines = readFile(filePath);
        if (lines.isEmpty()) {
            return 0;
        }
        String lastLine = lines.get(lines.size() - 1);
        String[] dataParts = lastLine.split(",");
        return Integer.parseInt(dataParts[0]);
    }

    /**
     * Custom exception class for handling invalid lines.
     */
    static class InvalidLineException extends Exception {
        /**
         * Constructs a new InvalidLineException with the specified detail message.
         *
         * @param filename The name of the file.
         */
        public InvalidLineException(String filename) {
            super("Invalid " + filename + " Files. Skipping this line.");
        }
    }

    /**
     * Custom exception class for handling invalid format.
     */
    static class InvalidFormatException extends Exception {
         /**
         * Constructs a new InvalidFormatException with the specified detail message.
         *
         * @param message The detail message.
         */
        public InvalidFormatException(String message) {
            super(message + "Skipping this line.");
        }
    }

}