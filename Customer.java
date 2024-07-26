import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a Customer user in the Ticket Management System.
*/
public class Customer extends User {

    private int numberOfCustomers = 0;

    public static final int MENU_SHOW_COST = 1;
    public static final int MENU_VIEW_SEAT = 2;
    public static final int MENU_BOOK_SEAT = 3;
    public static final int MENU_VIEW_BOOKING = 4;
    public static final int MENU_EXIT = 5;

    // Default
    public Customer() {
        super(); 
    }

    /**
     * Sets up the list of venue paths for the customer user.
     *
     * @param venuePaths the list of venue paths
     * @param filePaths the array of file paths
     * @param keyboard the Scanner object for user input
     * @return the updated list of venue paths
     */
    @Override
    public List<String> venuelists(List<String> venuePaths, String[] filePaths, Scanner keyboard) {
        int index = 0;
        setInfo(filePaths, keyboard);
        venuePaths.add("assets/venue_default.txt");

        // customerid is in command
        if (getCustomerId() > -1) {
            if (filePaths.length > 6) {
                index = 6;
            } else {
                return venuePaths;
            }
        } else { // customerid is not in command
            if (filePaths.length > 4) {
                index = 4;
            } else {
                return venuePaths;
            }
        }
        for (int i = index; i < filePaths.length; i++) {
            venuePaths.add(filePaths[i]);
        }
        return venuePaths;
    }

    /**
     * Handles exceptions during file processing for the customer user.
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
        for (int i = customerFileIndex; i < filePaths.length; i++) {
            String filePath = filePaths[i];

            try {
                List<String> lines = fileHandler.readFile(filePath); // check Exception 1
                File file = new File(filePath);
                String fileName = file.getName();
                fileHandler.checkInvalidLine(lines, fileName); // check Exception 2 & 3 (except Zoon Type)
                if (venuePaths.contains(filePath)) { // check Exception 3 - Venue Zoon type
                    fileHandler.checkZoonType(lines);
                }

                // if id is provided, check if password is correct and whether id exists
                if ((customerId > -1) && (i == customerFileIndex)) {
                    setName(lines, keyboard); // check Exception 4 & 5
                }

                // if id is not provided, create the new customer info
                if ((customerId == -1) && (i == customerFileIndex)) {
                    this.numberOfCustomers = lines.size();
                    // Case 3: Customer mode with no customer id and password
                    newcustomer(keyboard);
                    String writeInfo = customerId + "," + name + "," + customerPw;
                    fileHandler.writeFile(filePaths[customerFileIndex], writeInfo);
                }

                // load concerts information into concertlists when read the concert.csv file
                if ((concertFileIndex > -1) && (i == concertFileIndex)) {
                    concertDetails.setConcerts(concert.setConcerts(lines, concertDetails.getVenues()));
                }

                // load booking information into each venue in concert when read the booking.csv
                if (i == bookingFileIndex) {
                    for (String line : lines) {
                        String[] bookingParts = line.split(",");
                        int concertId = Integer.parseInt(bookingParts[3]);
                        int totaltickets = Integer.parseInt(bookingParts[4]);
                        int ticketId = 1;
                        for (ticketId = 1; ticketId <= totaltickets; ticketId++) {
                            int row = Integer.parseInt(bookingParts[5 * ticketId + 1]);
                            int col = Integer.parseInt(bookingParts[5 * ticketId + 2]);
                            char type = bookingParts[5 * ticketId + 3].charAt(0);
                            if (bookingParts[5 * ticketId + 3].charAt(0) == 'S') {
                                if (bookingParts[5 * ticketId + 3].charAt(1) == 'E') {
                                    type = 'S';
                                } else {
                                    type = 'T';
                                }
                            } else {
                                type = 'V';
                            }

                            concertDetails.getConcerts().get(concertId - 1).getVenue().bookSeat(row, col, type);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(filePath + " (No such file or directory)");
                return false;
            } catch (FileHandlerImpl.InvalidLineException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (FileHandlerImpl.InvalidFormatException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (Customer.IncorrectPasswordException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (Customer.NotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Displays the main menu for the Customer user and handles the menu options.
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
        System.out.printf("> ");
        boolean runLoop = true;
        while (runLoop) {
            printMenu();
            int choose = keyboard.nextInt();
            switch (choose) {
                case MENU_SHOW_COST:
                    concertDetails.ticketCosts(concertChoose);
                    break;
                case MENU_VIEW_SEAT:
                    concertDetails.viewLayout(concertChoose);
                    break;
                case MENU_BOOK_SEAT:
                    concertDetails.viewLayout(concertChoose);
                    concertDetails.bookSeats(user, filePaths[bookingFileIndex], keyboard, concertChoose);
                    break;
                case MENU_VIEW_BOOKING:
                    concertDetails.bookingDetails(user, filePaths[bookingFileIndex], concertChoose);
                    break;
                case MENU_EXIT:
                    System.out.println("Exiting this concert");
                    runLoop = false;
                    break;
                default:
                    System.out.println("Invalid Input.");
            }
        }
        System.out.printf("Select a concert or 0 to exit\n");
        concertDetails.showTimings();
        concertChoose = keyboard.nextInt();
        if (concertChoose == 0) {
            System.out.println("> Exiting customer mode");
            return;
        } else {
            user.mainMenu(user, filePaths, concertChoose, concertDetails, keyboard);
        }
    }

    /**
     * Prints the customer menu options.
     */
    private void printMenu() {
        System.out.println("Select an option to get started!");
        System.out.println("Press 1 to look at the ticket costs");
        System.out.println("Press 2 to view seats layout");
        System.out.println("Press 3 to book seats");
        System.out.println("Press 4 to view booking details");
        System.out.println("Press 5 to exit");
        System.out.print("> ");
    }

    /**
     * Sets the information for the customer user.
     *
     * @param filePaths the array of file paths
     * @param keyboard the Scanner object for user input
     */
    @Override
    public void setInfo(String[] filePaths, Scanner keyboard) {
        int Id = -1;
        // if id is provided, then set it; otherwise set as -1
        try {
            // check if customer id is in command
            this.customerId = Integer.parseInt(filePaths[1]);
        } catch (NumberFormatException e) {
            // customer id is not in command
            this.customerId = Id;
        }

        // check if customer id is in command and set all index
        if (customerId > -1) {
            this.customerId = Integer.parseInt(filePaths[1]);
            this.customerPw = filePaths[2];
            this.customerFileIndex = 3;
            this.concertFileIndex = 4;
            this.bookingFileIndex = 5;

        } else { // if customer id is not in command
            this.customerFileIndex = 1;
            this.concertFileIndex = 2;
            this.bookingFileIndex = 3;
        }
    }

    // Setters
    public void setCustomerName(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return this.name;
    }

    // check if customer is in customer file and whether password is correct
    public void setName(List<String> lines, Scanner keyboard) throws IncorrectPasswordException, NotFoundException {
        // iterates all lines in customer.csv
        for (String line : lines) {
            // customer id is provided
            if (this.customerId > -1) {
                String[] customerPart = line.split(",");
                int testId = Integer.parseInt(customerPart[0]);
                String testPw = customerPart[2];

                // customerId is in file
                if (customerId == testId) {
                    if (!customerPw.equals(testPw)) {
                        throw new IncorrectPasswordException();
                    } else {
                        name = customerPart[1];
                        return;
                    }
                }
            }
        }
        throw new NotFoundException(); // not find same customer
    }

    public void newcustomer(Scanner keyboard) {
        System.out.print("Enter your name: ");
        String customerName = keyboard.nextLine();
        System.out.print("Enter your password: ");
        String password = keyboard.nextLine();

        this.numberOfCustomers += 1;
        this.customerId = this.numberOfCustomers;
        this.name = customerName;
        this.customerPw = password;
    }

    // Custom exception class for 4. IncorrectPasswordException
    public static class IncorrectPasswordException extends Exception {
        public IncorrectPasswordException() {
            super("Incorrect Password. Terminating Program");
        }
    }

    // Custom exception class for 4. IncorrectPasswordException
    public static class NotFoundException extends Exception {
        public NotFoundException() {
            super("Customer does not exist. Terminating Program");
        }
    }
}
