import java.util.*;
import java.io.*;

public interface FileHandler {
    /**
     * Reads the content of a file.
     * 
     * @param filePath The path to the file to be read.
     * @return A list of strings representing the lines of the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    List<String> readFile(String filePath) throws IOException;

    /**
     * Reads the content of a file given its name.
     * 
     * @param filename The name of the file to be read.
     * @return A list of strings representing the lines of the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    static List<String> readFileName(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
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
     * @param filePath The path to the file to be written.
     * @param data     The data to be written to the file.
     */
    void writeFile(String filePath, String data);

    /**
     * Checks for invalid lines in a list of strings.
     * 
     * @param lines    The list of strings to be checked.
     * @param fileName The name of the file containing the lines.
     * @throws FileHandlerImpl.InvalidLineException    If an invalid line is found.
     * @throws FileHandlerImpl.InvalidFormatException If the format of the lines is invalid.
     */
    void checkInvalidLine(List<String> lines, String fileName)
            throws FileHandlerImpl.InvalidLineException, FileHandlerImpl.InvalidFormatException;

    /**
     * Checks the zone type in the lines of a file.
     * 
     * @param lines The list of strings representing the lines of the file.
     * @throws FileHandlerImpl.InvalidFormatException If the format of the zone type is invalid.
     */
    void checkZoonType(List<String> lines) throws FileHandlerImpl.InvalidFormatException;

    /**
     * Gets the last ID from a file.
     * 
     * @param filePath The path to the file.
     * @return The last ID found in the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    int getLastId(String filePath) throws IOException;

}