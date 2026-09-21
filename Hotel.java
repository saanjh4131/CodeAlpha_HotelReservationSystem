import java.util.ArrayList;

/**
 * Hotel.java
 * The "brain" of the program. It keeps the list of rooms and the list of bookings,
 * and has the methods to search rooms, create bookings and cancel bookings.
 * It does NOT print menus or read from the keyboard (that is Main's job).
 */
public class Hotel {

    private String name;
    private ArrayList<Room> rooms;
    private ArrayList<Booking> bookings;
    private FileManager fileManager;
    private int nextBookingNumber;   // used to make unique IDs: BK1001, BK1002, ...

    public Hotel(String name, FileManager fileManager) {
        this.name = name;
        this.fileManager = fileManager;
        this.rooms = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.nextBookingNumber = 1001;

        addDefaultRooms();
        loadSavedBookings();
    }

    // ---------- Setup ----------

    // Creates the default rooms so the program can be tested immediately
    private void addDefaultRooms() {
        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1500));
        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(203, "Deluxe", 2500));
        rooms.add(new Room(301, "Suite", 4500));
        rooms.add(new Room(302, "Suite", 4500));
    }

    // Loads bookings from the file, then:
    //  1) marks rooms of "Confirmed" bookings as booked (this is how availability is restored)
    //  2) makes sure new booking IDs continue after the highest saved ID
    private void loadSavedBookings() {
        bookings = fileManager.loadBookings();

        for (Booking booking : bookings) {
            if (booking.isConfirmed()) {
                Room room = findRoom(booking.getRoomNumber());
                if (room != null) {
                    room.setAvailable(false);
                }
            }

            // ID looks like "BK1005" -> take the number part "1005"
            String id = booking.getBookingId();
            if (id.length() > 2 && id.startsWith("BK")) {
                try {
                    int number = Integer.parseInt(id.substring(2));
                    if (number >= nextBookingNumber) {
                        nextBookingNumber = number + 1;
                    }
                } catch (NumberFormatException e) {
                    // ignore IDs that are not in the BK1234 format
                }
            }
        }
    }

    // ---------- Rooms ----------

    public String getName() {
        return name;
    }

    public ArrayList<Room> getAllRooms() {
        return rooms;
    }

    // Only the rooms that are free
    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                result.add(room);
            }
        }
        return result;
    }

    // All rooms of one category (available or not)
    public ArrayList<Room> getRoomsByCategory(String category) {
        ArrayList<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCategory().equalsIgnoreCase(category)) {
                result.add(room);
            }
        }
        return result;
    }

    // Free rooms of one category
    public ArrayList<Room> getAvailableRoomsByCategory(String category) {
        ArrayList<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCategory().equalsIgnoreCase(category) && room.isAvailable()) {
                result.add(room);
            }
        }
        return result;
    }

    // Finds a room by its number. Returns null if there is no such room.
    public Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // ---------- Bookings ----------

    // Finds a booking by its ID. Returns null if not found.
    public Booking findBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                return booking;
            }
        }
        return null;
    }

    // Makes a new unique ID like BK1001
    private String generateBookingId() {
        String id = "BK" + nextBookingNumber;
        nextBookingNumber++;
        return id;
    }

    // Creates a booking (called AFTER the payment is successful):
    //  - builds the Booking object
    //  - marks the room as unavailable
    //  - saves everything to the file
    public Booking createBooking(Room room, String customerName, String contact, int nights) {
        String bookingId = generateBookingId();
        double total = room.calculateTotal(nights);

        Booking booking = new Booking(bookingId, customerName, contact,
                room.getRoomNumber(), room.getCategory(), nights, total,
                Booking.PAYMENT_PAID, Booking.STATUS_CONFIRMED);

        bookings.add(booking);
        room.setAvailable(false);
        fileManager.saveBookings(bookings);

        return booking;
    }

    // Cancels a booking:
    //  - booking status becomes "Cancelled" (payment becomes "Refunded")
    //  - the room becomes available again
    //  - the file is updated
    // Returns false if the booking was already cancelled.
    public boolean cancelBooking(Booking booking) {
        if (!booking.isConfirmed()) {
            return false;
        }

        booking.setBookingStatus(Booking.STATUS_CANCELLED);
        booking.setPaymentStatus(Booking.PAYMENT_REFUNDED);

        Room room = findRoom(booking.getRoomNumber());
        if (room != null) {
            room.setAvailable(true);
        }

        fileManager.saveBookings(bookings);
        return true;
    }
}
