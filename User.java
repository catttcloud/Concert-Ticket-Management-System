import java.util.List;
import java.util.Scanner;

/**
 * Abstract class representing a user of the Ticket Management System.
 * This can be either an Admin or a Customer.
 */
public abstract class User {

    protected String mode;
    protected String name;
    protected int customerId;
    protected String customerPw;
    protected List<String> venuePaths;
    protected int customerFileIndex;
    protected int concertFileIndex;
    protected int bookingFileIndex;

    /**
     * Abstract method for setting up the list of venue paths.
     *
     * @param venuePaths the list of venue paths
     * @param filePaths the array of file paths
     * @param keyboard the Scanner object for user input
     * @return the updated list of venue paths
     */
    public abstract List<String> venuelists(List<String> venuePaths, String[] filePaths, Scanner keyboard);

    /**
     * Abstract method for handling exceptions during file processing.
     *
     * @param filePaths the array of file paths
     * @param fileHandler the FileHandler object for file operations
     * @param concertDetails the ConcertDetails object containing concert information
     * @param concert the Concert object
     * @param keyboard the Scanner object for user input
     * @return true if exceptions are handled successfully, false otherwise
     */
    public abstract boolean handleExceptions(String[] filePaths, FileHandler fileHandler, ConcertDetails concertDetails,
            Concert concert, Scanner keyboard);

    /**
     * Abstract method for setting user information.
     *
     * @param filePaths the array of file paths
     * @param keyboard the Scanner object for user input
     */
    public abstract void setInfo(String[] filePaths, Scanner keyboard);

    /**
     * Abstract method for displaying the main menu for the user.
     *
     * @param user the User object
     * @param filePaths the array of file paths
     * @param concertChoose the chosen concert ID
     * @param concertDetails the ConcertDetails object containing concert information
     * @param keyboard the Scanner object for user input
     */
    public abstract void mainMenu(User user, String[] filePaths, int concertChoose, ConcertDetails concertDetails, Scanner keyboard);

    /**
     * Default constructor for User class.
     */
    public User() { 
        this.customerId = -1;
        this.customerPw = "";
    }

    // Setters
    public void setmode(String mode) {
        this.mode = mode;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerPw(String customerPw) {
        this.customerPw = customerPw;
    }

    // Getters
    public int getCustomerId() {
        return this.customerId;
    }

    public String getCustomerPw() {
        return this.customerPw;
    }

    public String getName() {
        return this.name;
    }

    public String getmode() {
        return this.mode;
    }
}
