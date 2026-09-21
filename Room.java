/**
 * Room.java
 * Represents ONE hotel room (for example: room 102, Standard, Rs. 1500 per night).
 */
public class Room {

    // Private fields (encapsulation): they can only be used through the methods below
    private int roomNumber;
    private String category;      // "Standard", "Deluxe" or "Suite"
    private double pricePerNight;
    private boolean available;    // true = free to book, false = already booked

    // Constructor: runs when we create a room with "new Room(...)"
    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.available = true;    // every new room starts as available
    }

    // ----- Getters -----
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    // ----- Setter (only availability can change) -----
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Returns text to show in tables: "Available" or "Booked"
    public String getStatusText() {
        if (available) {
            return "Available";
        }
        return "Booked";
    }

    // Total price = price per night x number of nights
    public double calculateTotal(int nights) {
        return pricePerNight * nights;
    }
}
