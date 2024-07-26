import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * The TicketManagementEngine class is the entry point of the Ticket Management System.
 * It handles the initialization of the application, user mode selection (admin or customer),
 * and main loop for user interactions.
 */
public class TicketManagementEngine {
    private static final String ADMIN_MODE_FLAG = "--admin";
    private static final String CUSTOMER_MODE_FLAG = "--customer";
    // public static final int MENU_OPTION_6 = 6;
    // public static final int MENU_OPTION_7 = 7;

    /**
     * The main method initializes the Ticket Management System.
     * It selects the user mode, loads the necessary data, handles exceptions, and runs the user menu.
     *
     * @param args the command line arguments for specifying user mode and file paths
     */
    public static void main(String[] args) {
        TicketManagementEngine tme = new TicketManagementEngine();
        Scanner keyboard = new Scanner(System.in);
        FileHandler fileHandler = new FileHandlerImpl();
        ConcertDetails concertDetails = new ConcertDetails();
        Concert concert = new Concert();
        boolean exit = false;

        // Default data
        String[] filePaths = args;
        User user = tme.selectUserMode(filePaths);
        List<String> venuePaths = new ArrayList<>();

        if (user == null || filePaths.length == 0) {
            System.out.println("Invalid user mode. Terminating program now.");
            keyboard.close();
            return;
        }

        // Load the venue information
        user.venuePaths = user.venuelists(venuePaths, filePaths, keyboard);
        concertDetails = concertDetails.loadVenues(venuePaths);
        int choose = -1;

        // Handling exceptions
        if (!user.handleExceptions(filePaths, fileHandler, concertDetails, concert, keyboard)) {
            keyboard.close();
            return;
        }

        // Deal with customer
        // Case 1: Customer mode with customer id and correct password
        if (user.customerId > -1) {
            System.out.printf("Welcome %s to Ticket Management System\n", ((Customer) user).getName());
            // the welcome message need to have the customer name or admin mode mentioned.
            tme.displayMessage();
            System.out.printf("Select a concert or 0 to exit\n");
            concertDetails.showTimings();
            choose = keyboard.nextInt();
        } else if (user.getmode() == "Admin") { // admin mode
            System.out.printf("Welcome to Ticket Management System Admin Mode.\n");
            // the welcome message need to have the customer name or admin mode mentioned.
            tme.displayMessage();
        }

        // Run the user menu
        while (!exit) {
            if ((choose == 0)) {
                exit = true;
                System.out.println("> Exiting customer mode");
                keyboard.close();
                return;
            }
            user.mainMenu(user, filePaths, choose, concertDetails, keyboard);
            exit = true;
        }

    }

    /**
     * Displays a welcome message in ASCII art.
     */
    public void displayMessage() {
        System.out.print("\n" +
                " ________  ___ _____ \n" +
                "|_   _|  \\/  |/  ___|\n" +
                "  | | | .  . |\\ `--. \n" +
                "  | | | |\\/| | `--. \\\n" +
                "  | | | |  | |/\\__/ /\n" +
                "  \\_/ \\_|  |_/\\____/ \n" +
                "                    \n" +
                "                    \n");
    }

    /**
     * Selects the user mode based on the command line arguments.
     *
     * @param args the command line arguments
     * @return the User object representing either an Admin or a Customer
     */
    private User selectUserMode(String[] args) {
        User user = null;
        String input = args[0];
        switch (input) {
            case CUSTOMER_MODE_FLAG:
                user = new Customer();
                user.setmode("Customer");
                break;
            case ADMIN_MODE_FLAG:
                user = new Admin();
                user.setmode("Admin");
                break;
            default:
                user = null;
                break;
        }
        return user;
    }

}
