import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents an Admin user in the Ticket Management System.
 */
public class Admin extends User {

    public int isVenue = 4; // if args.length smaller than 4, then no venue is provided
    public static final int MENU_CONCERT_DETAILS = 1;
    public static final int MENU_UPDATE_COST = 2;
    public static final int MENU_VIEW_BOOKING = 3;
    public static final int MENU_TOTAL_PAYMENT = 4;
    public static final int MENU_EXIT = 5;

    // Default
    public Admin() {
        super();
    }

    /**
     * Displays the main menu for the Admin user and handles the menu options.
     *
     * @param user the User object
     * @param filePaths the array of file paths
     * @param concertChoose the chosen concert ID
     * @param concertDetails the ConcertDetails object containing concert information
     * @param keyboard the Scanner object for user input
     */
    @Override
    public void mainMenu(User user, String[] filePaths, int concertChoose, ConcertDetails concertDetails,
            Scanner keyboard) {
        boolean runLoop = true;
        printMenu();
        int choose = keyboard.nextInt();
        while (runLoop) {
            switch (choose) {
                case MENU_CONCERT_DETAILS:
                    System.out.printf("> ");
                    concertDetails.showTimings();
                    break;
                case MENU_UPDATE_COST:
                    concertDetails.updateCost(keyboard);
                    break;
                case MENU_VIEW_BOOKING:
                    concertDetails.viewBooking(user, filePaths[bookingFileIndex], keyboard);
                    break;
                case MENU_TOTAL_PAYMENT:
                    concertDetails.totalPrice(keyboard, user, filePaths[bookingFileIndex]);
                    break;
                case MENU_EXIT:
                    System.out.println("> Exiting admin mode");
                    runLoop = false;
                    return;
                default:
                    System.out.println("> Invalid Input");
            }
            printMenu();
            choose = keyboard.nextInt();
        }
    }

    /**
     * Prints the admin menu options.
    */
    private void printMenu() {
        System.out.println("Select an option to get started!");
        System.out.println("Press 1 to view all the concert details");
        System.out.println("Press 2 to update the ticket costs");
        System.out.println("Press 3 to view booking details");
        System.out.println("Press 4 to view total payment received for a concert");
        System.out.println("Press 5 to exit");
    }

    /**
     * Sets the information for the admin user.
     *
     * @param filePaths the array of file paths
     * @param keyboard the Scanner object for user input
     */
    @Override
    public void setInfo(String[] filePaths, Scanner keyboard) {
        this.customerId = -1;
        this.customerFileIndex = 1;
        this.concertFileIndex = 2;
        this.bookingFileIndex = 3;
    }

    /**
     * Sets up the list of venue paths for the admin user.
     *
     * @param venuePaths the list of venue paths
     * @param filePaths the array of file paths
     * @param keyboard the Scanner object for user input
     * @return the updated list of venue paths
     */
    @Override
    public List<String> venuelists(List<String> venuePaths, String[] filePaths, Scanner keyboard) {
        venuePaths.add("assets/venue_default.txt");
        if (filePaths.length > 4)
            for (int i = 4; i < filePaths.length; i++) {
                venuePaths.add(filePaths[i]);
            }
        return venuePaths;
    }

    /**
     * Handles exceptions during file processing for the admin user.
     *
     * @param filePaths the array of file paths
     * @param fileHandler the FileHandler object for file operations
     * @param concertDetails the ConcertDetails object containing concert information
     * @param concert the Concert object
     * @param keyboard the Scanner object for user input
     * @return true if exceptions are handled successfully, false otherwise
     */
    @Override
    public boolean handleExceptions(String[] filePaths, FileHandler fileHandler, ConcertDetails concertDetails,
            Concert concert, Scanner keyboard) {
        setInfo(filePaths, keyboard);
        for (int i = 1; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            try {
                List<String> lines = fileHandler.readFile(filePath); // check Exception 1
                File file = new File(filePath);
                String fileName = file.getName();
                fileHandler.checkInvalidLine(lines, fileName); // check Exception 2

                // load concerts information into concertlists when read the concert.csv file
                if (i == concertFileIndex) {
                    concertDetails.setConcerts(concert.setConcerts(lines, concertDetails.getVenues()));
                }

                // load booking information into each venue in concert when read the booking.csv
                // file
                if (i == bookingFileIndex) {

                    for (String line : lines) {
                        String[] bookingParts = line.split(",");
                        // invalid line - Skipping
                        try {
                            if (bookingParts.length < 5) {
                                throw new FileHandlerImpl.InvalidLineException("booking");
                            }
                        } catch (FileHandlerImpl.InvalidLineException e) {
                            System.out.println(e.getMessage());
                            continue;
                        }
                        // buy 0 tickets - Skipping
                        if (Integer.parseInt(bookingParts[4]) == 0) {
                            System.out.println("Incorrect Number of Tickets. Skipping this line.");
                            continue;
                        }
                        int concertId = Integer.parseInt(bookingParts[3]);
                        int totaltickets = Integer.parseInt(bookingParts[4]);
                        if (concertId == concertDetails.getConcerts().get(concertId - 1).getconcertId()) {
                            int ticketId = 1;
                            for (ticketId = 1; ticketId <= totaltickets; ticketId++) {
                                int row = Integer.parseInt(bookingParts[5 * ticketId + 1]);
                                int col = Integer.parseInt(bookingParts[5 * ticketId + 2]);
                                char type = bookingParts[5 * ticketId + 3].charAt(0);
                                concertDetails.getConcerts().get(concertId - 1).getVenue().bookSeat(row, col, type);
                            }
                        }

                    }
                }
                if (this.venuePaths.contains(filePath)) { // check Exception 3 - Venue Zoon type
                    fileHandler.checkZoonType(lines);
                }
            } catch (IOException e) {
                System.out.println(filePath + " (No such file or directory)");
                return false;
            } catch (FileHandlerImpl.InvalidLineException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (FileHandlerImpl.InvalidFormatException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
        return true;
    }

}
