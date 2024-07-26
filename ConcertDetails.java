import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class represents details about concerts, including show timings, ticket costs,
 * booking information, and more.
 */
public class ConcertDetails {
    private List<Concert> concerts = new ArrayList<>();
    private List<Venue> venues = new ArrayList<>();

    /**
     * Method to display the show timings of all concerts.
     */
    public void showTimings() {

        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s%-15s%-15s%-15s%-30s%-15s%-15s%-15s\n", "#", "Date", "Artist Name", "Timing",
                "Venue Name", "Total Seats", "Seats Booked", "Seats Left");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");

        for (Concert concert : getConcerts()) {
            if (concert.getconcertId() != 0) {
                System.out.printf("%-5d%-15s%-15s%-15s%-30s%-15d%-15d%-15d\n", concert.getconcertId(),
                        concert.getDate(),
                        concert.getArtist(), concert.getTiming(),
                        concert.getVenueName(), concert.getVenue().getTotalSeats(), concert.getVenue().getBookSeats(),
                        concert.getVenue().getLeftSeats());
            }
        }
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays the ticket costs for the specified concert.
     * 
     * @param concert The index of the concert to display ticket costs for.
     */
    public void ticketCosts(int concert) {
        String[] type = { "SEATING", "STANDING", "VIP" }; // Seating(S) - Standing(T) - VIP(V)
        int[] index = { 1, 0, 2 };
        for (int i : index) {
            System.out.printf("---------- %8s ----------%n", type[i]);
            System.out.printf("Left Seats:   %-4.1f\n", concerts.get(concert - 1).getPrices()[i][0]);
            System.out.printf("Center Seats: %-4.1f\n", concerts.get(concert - 1).getPrices()[i][1]);
            System.out.printf("Right Seats:  %-4.1f\n", concerts.get(concert - 1).getPrices()[i][2]);
            System.out.println("------------------------------");
        }
    }

    /**
     * Displays the layout of the venue for the specified concert.
     * 
     * @param concert The index of the concert to view the layout for.
     */
    public void viewLayout(int concert) {
        concerts.get(concert - 1).getVenue().printSeats();
    }

    /**
     * Books seats for the specified user in the specified concert.
     * 
     * @param user The user for whom seats are being booked.
     * @param bookingPath The path to the booking file.
     * @param keyboard The scanner object for user input.
     * @param concert The index of the concert for which seats are being booked.
     */
    public void bookSeats(User user, String bookingPath, Scanner keyboard, int concert) {
        FileHandler fileHandler = new FileHandlerImpl();
        System.out.printf("Enter the aisle number: ");
        String aisle = keyboard.next();

        System.out.print("Enter the seat number: ");
        int seat = keyboard.nextInt();

        System.out.print("Enter the number of seats to be booked: ");
        int numbers = keyboard.nextInt();

        // updates the concert venue
        int row = Integer.parseInt(aisle.substring(1));
        int leftcols = this.concerts.get(concert - 1).getVenue().getleftCols();
        int middlecols = this.concerts.get(concert - 1).getVenue().getmiddleCols();
        int bookingid = bookingId(user.getCustomerId(), concert, bookingPath);

        String data = Integer.toString(bookingid + 1) + "," + user.getCustomerId() + "," + user.getName()
                + "," + String.valueOf(concert) + "," + String.valueOf(numbers);

        // for each seat choose
        for (int num = 1; num < numbers + 1; num++) {
            int i = 0;
            int j = 0;
            String type = "";

            // find the seat type Seating(S) - Standing(T) - VIP(V)
            if (Character.toString(aisle.charAt(0)).equals("S")) {
                i = 0;
                type = "SEATING";
            } else if (Character.toString(aisle.charAt(0)).equals("T")) {
                i = 1;
                type = "STANDING";
            } else if (Character.toString(aisle.charAt(0)).equals("V")) {
                i = 2;
                type = "VIP";
            }

            // decide whether the seat is left - middle - right
            if (seat + num - 1 < leftcols + 1) {
                j = 0;
            } else if (seat + num - 1 < leftcols + middlecols + 1) {
                j = 1;
            } else {
                j = 2;
            }
            // Seating(S) - Standing(T) - VIP(V)
            this.concerts.get(concert - 1).getVenue().bookSeat(row, seat + num - 1, aisle.charAt(0));

            String dataAdd = "," + String.valueOf(num) + "," + aisle.charAt(1) + ","
                    + String.valueOf(seat + num - 1) + "," + type + ","
                    + (int) concerts.get(concert - 1).getPrices()[i][j].doubleValue();
            this.concerts.get(concert - 1).getVenue().bookSeat((int) aisle.charAt(1), seat + num - 1, aisle.charAt(0));
            data += dataAdd;
        }
        fileHandler.writeFile(bookingPath, data);
    }

    /**
     * Displays booking details for the specified user and concert.
     * 
     * @param user The user for whom booking details are being displayed.
     * @param bookingPath The path to the booking file.
     * @param concertchoose The index of the concert for which booking details are being displayed.
     */
    public void bookingDetails(User user, String bookingPath, int concertchoose) {
        List<Object> mixedList = mixedList(user, bookingPath, concertchoose);
        List<Integer> bookingIds = (List<Integer>) mixedList.get(0);
        List<Integer> bookingConcerts = (List<Integer>) mixedList.get(1);
        List<Integer> bookingTicketsnum = (List<Integer>) mixedList.get(2);
        List<Double> bookingPrices = (List<Double>) mixedList.get(3);
        List<List<String>> ticketsInfo = (List<List<String>>) mixedList.get(4);

        if (bookingConcerts.size() == 0 || bookingTicketsnum.get(0) == 0) {
            System.out.println("No Bookings found for this concert\n");
            return;
        }
        printbookingConcert(bookingIds, bookingConcerts, bookingTicketsnum, bookingPrices);
        printTicketsInfo(bookingIds, ticketsInfo);

    }

    /**
     * Prints booking details for the specified bookings.
     * 
     * @param bookingIds The list of booking IDs.
     * @param bookingConcerts The list of concert IDs for the bookings.
     * @param bookingTicketsnum The list of ticket numbers for the bookings.
     * @param bookingPrices The list of total prices for the bookings.
     */
    public void printbookingConcert(List<Integer> bookingIds, List<Integer> bookingConcerts,
            List<Integer> bookingTicketsnum,
            List<Double> bookingPrices) {
        System.out.println("Bookings");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s%-15s%-15s%-10s%-15s%-15s%-10s\n", "Id", "Concert Date", "Artist Name", "Timing",
                "Venue Name", "Seats Booked", "Total Price");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < bookingConcerts.size(); i++) {
            int concertnum = bookingConcerts.get(i);
            Concert concert = this.concerts.get(concertnum - 1);
            System.out.printf("%-5d%-15s%-15s%-10s%-15s%-15d%-10.1f\n", bookingIds.get(i), concert.getDate(),
                    concert.getArtist(),
                    concert.getTiming(),
                    concert.getVenueName(), bookingTicketsnum.get(i), bookingPrices.get(i));
        }
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    /**
     * Prints ticket information for the specified bookings.
     * 
     * @param bookingIds The list of booking IDs.
     * @param ticketsInfo The list of ticket information for the bookings.
     */
    public void printTicketsInfo(List<Integer> bookingIds, List<List<String>> TicketsInfo) {
        System.out.println("Ticket Info");

        for (int i = 0; i < TicketsInfo.size(); i++) {
            System.out.printf("############### Booking Id: %d ####################\n", bookingIds.get(i));
            System.out.printf("%-5s%-15s%-15s%-10s%-10s\n", "Id", "Aisle Number", "Seat Number", "Seat Type", "Price");
            System.out.println("##################################################");
            for (String ticketInfo : TicketsInfo.get(i)) {
                System.out.println(ticketInfo);
            }
            System.out.println("##################################################");
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Generates a mixed list containing booking details for the specified user and concert.
     * 
     * @param user The user for whom booking details are being generated.
     * @param bookingPath The path to the booking file.
     * @param concertchoose The index of the concert for which booking details are being generated.
     * @return A mixed list containing booking details.
     */
    public <T> List<T> mixedList(User user, String bookingPath, int concertchoose) {
        List<T> mixedList = new ArrayList<>();
        FileHandler fileHandler = new FileHandlerImpl();
        int userId = user.getCustomerId();
        try {
            // booking info
            List<String> lines = fileHandler.readFile(bookingPath);
            List<Integer> bookingIds = new ArrayList<>(); // save booking id
            List<Integer> bookingConcerts = new ArrayList<>(); // save concert id
            List<Integer> bookingTicketsnum = new ArrayList<>(); // save total tickets number
            List<Double> bookingPrices = new ArrayList<>(); // save price for each ticke
            List<List<String>> TicketsInfo = new ArrayList<>(); // tickets information data

            // for each booking line
            for (String line : lines) {
                String[] bookingParts = line.split(",");

                if (bookingParts.length < 5) {
                    continue;
                }
                int customerId = Integer.parseInt(bookingParts[1]);

                boolean isCustomerMatch = (user.getmode().equals("Customer") && (customerId == userId)
                        && (concertchoose == Integer.parseInt(bookingParts[3])));
                boolean isAdminMatch = (user.getmode().equals("Admin")
                        && concertchoose == Integer.parseInt(bookingParts[3]));

                if (isCustomerMatch || isAdminMatch) {
                    // if is same customer or admin-choosed concert
                    List<String> bookingTicketsInfo = new ArrayList<>();

                    // save booking id
                    int bookingId = Integer.parseInt(bookingParts[0]);
                    bookingIds.add(bookingId);

                    // save concert details to bookingconcerts
                    int concertId = Integer.parseInt(bookingParts[3]);
                    bookingConcerts.add(concertId);

                    int seatsBooked = Integer.parseInt(bookingParts[4]);
                    bookingTicketsnum.add(seatsBooked);

                    int ticketId = 1;

                    double totalPrice = 0;

                    // iterate tickets info into list
                    for (ticketId = 1; ticketId <= seatsBooked; ticketId++) {
                        int row = Integer.parseInt(bookingParts[5 * ticketId + 1]);
                        int col = Integer.parseInt(bookingParts[5 * ticketId + 2]);
                        String type = bookingParts[5 * ticketId + 3];
                        double price = Double.parseDouble(bookingParts[5 * ticketId + 4]);
                        totalPrice += price;
                        String data = String.format("%-5d%-15d%-15d%-10s%-10.1f", ticketId, row, col, type, price);
                        bookingTicketsInfo.add(data);

                    }
                    bookingPrices.add(totalPrice);
                    TicketsInfo.add(bookingTicketsInfo);
                }
            }
            mixedList.add((T) bookingIds);
            mixedList.add((T) bookingConcerts);
            mixedList.add((T) bookingTicketsnum);
            mixedList.add((T) bookingPrices);
            mixedList.add((T) TicketsInfo);
        } catch (IOException e) {
            e.getMessage();
        }
        return mixedList;
    }

    /**
     * Retrieves the booking ID for the specified customer and concert.
     * 
     * @param customerId The ID of the customer.
     * @param concert The index of the concert.
     * @param bookingPath The path to the booking file.
     * @return The booking ID.
     */
    public int bookingId(int customerId, int concert, String bookingPath) {
        FileHandler fileHandler = new FileHandlerImpl();
        int bookingid = 1;
        try {
            List<String> lines = fileHandler.readFile(bookingPath);
            bookingid = 0;
            for (String line : lines) {
                String[] bookingParts = line.split(",");
                if ((customerId == Integer.parseInt(bookingParts[1]))
                        && (concert == Integer.parseInt(bookingParts[3]))) {
                    bookingid = Integer.parseInt(bookingParts[0]);
                }
            }

        } catch (IOException e) {
            e.getMessage();
        }
        return bookingid;
    }

    // admin model

    /**
     * Updates the cost of tickets for a specific zone in a concert.
     * 
     * @param keyboard The scanner object for user input.
     */
    public void updateCost(Scanner keyboard) {
        System.out.println("> Select a concert or 0 to exit");
        showTimings();
        int concertchoose = keyboard.nextInt();
        System.out.printf("> ");
        ticketCosts(concertchoose);

        System.out.printf("Enter the zone : VIP, SEATING, STANDING: ");
        String zoon = keyboard.next();
        System.out.println();

        System.out.print("Left zone price: ");
        int newleft = keyboard.nextInt();

        System.out.print("Centre zone price: ");
        int newmiddle = keyboard.nextInt();

        System.out.print("Right zone price: ");
        int newright = keyboard.nextInt();

        concerts.get(concertchoose - 1).updatePrice(zoon, newleft, newmiddle, newright);
    }

    /**
     * Displays booking details for a specific concert chosen by the user.
     * 
     * @param user The user for whom booking details are being displayed.
     * @param bookingPath The path to the booking file.
     * @param keyboard The scanner object for user input.
     */
    public void viewBooking(User user, String bookingPath, Scanner keyboard) {
        System.out.println("> Select a concert or 0 to exit");
        showTimings();
        System.out.printf("> ");
        int concertchoose = keyboard.nextInt();
        bookingDetails(user, bookingPath, concertchoose);
    }

    /**
     * Calculates and displays the total price for a specific concert chosen by the user.
     * 
     * @param keyboard The scanner object for user input.
     * @param user The user for whom the total price is being calculated.
     * @param bookingPath The path to the booking file.
     */
    public void totalPrice(Scanner keyboard, User user, String bookingPath) {
        System.out.println("Select a concert or 0 to exit");
        showTimings();
        System.out.printf("> ");
        int concertchoose = keyboard.nextInt();
        double totalPrice = 0;

        List<Object> mixedList = mixedList(user, bookingPath, concertchoose);
        List<Integer> bookingConcerts = (List<Integer>) mixedList.get(0);
        List<Double> bookingPrices = (List<Double>) mixedList.get(2);

        for (int i = 0; i < bookingConcerts.size(); i++) {
            int concertnum = bookingConcerts.get(i);
            if (concertnum == concertchoose) {
                totalPrice += bookingPrices.get(i);
            }
        }
        System.out.printf("Total Price for this concert is AUD %.1f\n", totalPrice);
    }

    /**
     * Adds a venue to the list of venues.
     * 
     * @param venue The venue to be added.
     */
    public void addVenue(Venue venue) {
        this.venues.add(venue);
    }

    /**
     * Loads venues from the specified list of venue paths.
     * 
     * @param venuePaths The list of paths to venue files.
     * @return A ConcertDetails object with venues loaded.
     */
    public ConcertDetails loadVenues(List<String> venuePaths) {
        ConcertDetails concertDetails = new ConcertDetails();
        FileHandlerImpl fileHandler = new FileHandlerImpl();
        try {
            for (String venuePath : venuePaths) {
                Venue venue = new Venue();
                File file = new File(venuePath);
                String fileName = file.getName();

                String venueName = fileName.split("\\.")[0].split("_")[1];

                List<String> lines = fileHandler.readFile(venuePath);

                venue = venue.loadVenue(lines);

                venue.setVenuename(venueName);

                concertDetails.addVenue(venue);
            }

        } catch (IOException e) {
            e.getMessage();
        }
        return concertDetails;
    }

    // setter
    public void setConcerts(List<Concert> concerts) {
        this.concerts = concerts;
    }

    // getter
    public List<Concert> getConcerts() {
        return this.concerts;
    }

    public List<Venue> getVenues() {
        return this.venues;
    }

}
