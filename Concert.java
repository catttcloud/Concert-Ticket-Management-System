import java.util.*;

public class Concert {
    private int concertId;
    private String concertDate;
    private String concertTiming;
    private String artistName;
    private String venueName;
    private Venue venue;
    private Double[][] seatPrices = new Double[3][3]; // Seating(S) - Standing(T) - VIP(V)

    private static int CONCERT_ID_INDEX = 0;
    private static int CONCERT_DATE_INDEX = 1;
    private static int CONCERT_TIME_INDEX = 2;
    private static int ARTIST_NAME_INDEX = 3;
    private static int VENUE_NAME_INDEX = 4;
    private static int STANDING_PRICE_INDEX = 5;
    private static int SEATING_PRICE_INDEX = 6;
    private static int VIP_PRICE_INDEX = 7;

    public Concert() {
        // default
    }

    public Concert(int concertId, String concertDate, String concertTiming, String artistName, String venueName) {
        this.concertId = concertId;
        this.concertTiming = concertTiming;
        this.concertDate = concertDate;
        this.artistName = artistName;
        this.venueName = venueName;
    }

    // setters
    public void setvenue(Venue venue) {
        this.venue = venue;
    }

    public void setprice(String[] seatingPrices, String[] standingPrices, String[] vipPrices) {
        for (int col = 1; col < 4; col++) {
            this.seatPrices[0][col - 1] = Double.parseDouble(seatingPrices[col]);
            this.seatPrices[1][col - 1] = Double.parseDouble(standingPrices[col]);
            this.seatPrices[2][col - 1] = Double.parseDouble(vipPrices[col]);
        }
    }

    public void updatePrice(String type, double newleft, double newmiddle, double newright) {
        int i = 0;
        if (type.equals("SEATING")) {
            i = 0;
        } else if (type.equals("STANDING")) {
            i = 1;
        } else {
            i = 2;
        }
        this.seatPrices[i][0] = newleft;
        this.seatPrices[i][1] = newmiddle;
        this.seatPrices[i][2] = newright;
    }

    public List<Concert> setConcerts(List<String> lines, List<Venue> venues) {
        List<Concert> concerts = new ArrayList<>();

        for (String line : lines) {
            String[] concertInfo = line.split(",");

            try {
                if (!FileHandlerImpl.checkInvalidFormat(concertInfo, 0)) {
                    throw new FileHandlerImpl.InvalidFormatException("Concert Id is in incorrect format. ");
                }
            } catch (FileHandlerImpl.InvalidFormatException e) {
                Concert newConcert = new Concert();
                concerts.add(newConcert); // add null
                System.out.println(e.getMessage());
                continue;
            }

            // save basic concert information
            Concert newConcert = new Concert(Integer.parseInt(concertInfo[CONCERT_ID_INDEX]),
                    concertInfo[CONCERT_DATE_INDEX],
                    concertInfo[CONCERT_TIME_INDEX], concertInfo[ARTIST_NAME_INDEX], concertInfo[VENUE_NAME_INDEX]);
            // save each price
            String[] standingPrice = concertInfo[STANDING_PRICE_INDEX].split(":");
            String[] seatingPrice = concertInfo[SEATING_PRICE_INDEX].split(":");
            String[] vipPrice = concertInfo[VIP_PRICE_INDEX].split(":");
            newConcert.setprice(seatingPrice, standingPrice, vipPrice);
            newConcert.setvenue(venues.get(0)); // use default venue

            // save venue information
            for (Venue venue : venues) {
                // if concert venue has venue file
                if (venue.getVenuename().toUpperCase().equals(concertInfo[VENUE_NAME_INDEX])) {
                    Venue newvenue = new Venue(venue.getrows(), venue.getleftCols(), venue.getmiddleCols(),
                            venue.getrightCols());
                    newvenue.setnewvenueseats(venue);
                    newConcert.setvenue(newvenue);
                    break;
                }
            }
            concerts.add(newConcert);
        }
        return concerts;
    }

    // getters
    public int getconcertId() {
        return this.concertId;
    }

    public String getDate() {
        return this.concertDate;
    }

    public String getArtist() {
        return this.artistName;
    }

    public String getTiming() {
        return this.concertTiming;
    }

    public String getVenueName() {
        return this.venueName;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public Double[][] getPrices() {
        return this.seatPrices;
    }
}
