import java.util.List;

/**
 * The Venue class represents a venue with seating arrangements.
 */
public class Venue {
    private String venueName;
    /** The number of rows in the venue. */
    private int rows;
    /** The number of seats in the left zone. */
    private int leftCols;
    /** The number of seats in the middle zone. */
    private int middleCols;
    /** The number of seats in the right zone. */
    private int rightCols;
    /** A two-dimensional array representing the seating layout of the venue. */
    private char[][] seats;
    /** The total number of seats in the venue. */
    private int totalSeats;
    /** The number of seats that have been booked. */
    private int bookedSeats;
    /** The number of seats that are available for booking. */
    private int leftSeats;

    public Venue() {
        // default
    }

    /**
     * Parameterized constructor for the Venue class.
     *
     * @param rows       The number of rows in the venue.
     * @param leftCols   The number of seats in the left zone.
     * @param middleCols The number of seats in the middle zone.
     * @param rightCols  The number of seats in the right zone.
     */
    public Venue(int rows, int leftCols, int middleCols, int rightCols) {
        this.rows = rows;
        this.leftCols = leftCols;
        this.middleCols = middleCols;
        this.rightCols = rightCols;
        this.totalSeats = (rows - 2) * (leftCols + middleCols + rightCols);
        this.bookedSeats = 0;
        this.leftSeats = totalSeats;
        this.seats = new char[rows][leftCols + middleCols + rightCols + 6];
        initializeSeats();
    }

    public void setnewvenueseats(Venue venue) {
        this.seats = venue.getseats();
    }

    /**
     * Initializes the seating layout of the venue.
     */
    private void initializeSeats() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < (leftCols + middleCols + rightCols + 6); j++) {
                this.seats[i][j] = ' ';
            }
        }

    }

    /**
     * Loads the seats into the venue from the given list of lines.
     *
     * @param venue The Venue object.
     * @param lines The list of lines representing the seating layout.
     */
    private void loadSeats(Venue venue, List<String> lines) {
        // for each line
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (!line.isEmpty()) {

                String[] lineParts = line.split(" ");
                char type = lineParts[0].charAt(0);
                venue.setSeat(venue, i, 0, type); // set the first char V/S/T

                // set left cols
                String leftcol = lineParts[1].replace("[", "").replace("]", "");
                for (int col = 2; col < 2 + venue.getleftCols(); col++) {
                    venue.setSeat(venue, i, col, leftcol.charAt(col - 2));
                }
                // set middle cols
                String middlecol = lineParts[2].replace("[", "").replace("]", "");
                for (int col = venue.getleftCols() + 3; col < venue.getleftCols() + venue.getmiddleCols() + 3; col++) {
                    venue.setSeat(venue, i, col, middlecol.charAt(col - venue.getleftCols() - 3));
                }
                // set right cols
                String rightcol = lineParts[3].replace("[", "").replace("]", "");
                for (int col = venue.getleftCols() + venue.getmiddleCols() + 4; col < venue.getleftCols()
                        + venue.getmiddleCols() + venue.getrightCols() + 4; col++) {
                    venue.setSeat(venue, i, col,
                            rightcol.charAt(col - venue.getleftCols() - venue.getmiddleCols() - 4));
                }
                setSeat(venue, i, venue.getleftCols() + venue.getmiddleCols() + venue.getrightCols() + 5, type);                                                                                             // V/S/T
            }
        }
    }

    /**
     * Prints the seating layout of the venue.
     */
    public void printSeats() {
        int num = 1;
        for (int i = 0; i < rows; i++) {
            if (this.seats[i][0] == ' ') {
                num = 1;
            } else {
                System.out.print(this.seats[i][0]);
                System.out.print(num);

                for (int j = 1; j < (leftCols + middleCols + rightCols + 5); j++) {
                    if (this.seats[i][j] != ' ') {
                        System.out.print("[" + this.seats[i][j] + "]");
                    } else {
                        System.out.print(this.seats[i][j]);
                    }
                }
                System.out.print(this.seats[i][leftCols + middleCols + rightCols + 5]);
                System.out.print(num);
                num += 1;
            }
            System.out.println();

        }
    }

    /**
     * Loads the seating layout of the venue from the given list of lines.
     *
     * @param lines The list of lines representing the seating layout.
     * @return The Venue object with loaded seating layout.
     */
    public Venue loadVenue(List<String> lines) {
        String[] firstLineParts = lines.get(0).split(" ");
        int leftCols = firstLineParts[1].replace("[", "").length() / 2;
        int middleCols = firstLineParts[2].replace("[", "").length() / 2;
        int rightCols = firstLineParts[3].replace("[", "").length() / 2;
        int rows = lines.size();
        Venue venue = new Venue(rows, leftCols, middleCols, rightCols);
        loadSeats(venue, lines);
        return venue;
    }

    /**
     * Books a seat in the venue.
     *
     * @param row  The row number of the seat.
     * @param col  The column number of the seat.
     * @param type The type of the seat (V/S/T).
     */
    public void bookSeat(int row, int col, char type) {
        int num = 0;
        String stringcol = Integer.toString(col);
        for (int i = 0; i < this.rows; i++) {
            // zoon type
            if (type == this.seats[i][0]) {
                num++;
                // row achieves
                if (row == num) {
                    for (int j = 0; j < leftCols + middleCols + rightCols + 6; j++) {
                        String stringseats = String.valueOf(this.seats[i][j]);
                        if (stringcol.equals(stringseats)) {
                            this.seats[i][j] = 'X';
                            this.bookedSeats += 1;
                            this.leftSeats -= 1;
                            break;
                        }
                    }
                }
            }

        }
    }

    // getters
    public String getVenuename() {
        return this.venueName;
    }

    public int getTotalSeats() {
        return this.totalSeats;
    }

    public int getBookSeats() {
        return this.bookedSeats;
    }

    public int getLeftSeats() {
        return this.leftSeats;
    }

    public int getleftCols() {
        return this.leftCols;
    }

    public int getmiddleCols() {
        return this.middleCols;
    }

    public int getrightCols() {
        return this.rightCols;
    }

    public int getrows() {
        return this.rows;
    }

    public char[][] getseats() {
        return this.seats;
    }

    // setters
    public void setVenuename(String venueName) {
        this.venueName = venueName;
    }

    public void setSeat(Venue venue, int row, int col, char type) {
        venue.seats[row][col] = type;
    }
}
