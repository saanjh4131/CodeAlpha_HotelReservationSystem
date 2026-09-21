/**
 * Booking.java
 * Represents ONE reservation made by a customer.
 * A booking is saved in the file as one line, with the fields separated by "|".
 */
public class Booking {

    // Constants, so we never make a spelling mistake in the status text
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String PAYMENT_PAID = "Paid";
    public static final String PAYMENT_REFUNDED = "Refunded";

    private String bookingId;
    private String customerName;
    private String contact;
    private int roomNumber;
    private String roomType;
    private int nights;
    private double totalAmount;
    private String paymentStatus;   // "Paid" or "Refunded"
    private String bookingStatus;   // "Confirmed" or "Cancelled"

    // Constructor
    public Booking(String bookingId, String customerName, String contact,
                   int roomNumber, String roomType, int nights,
                   double totalAmount, String paymentStatus, String bookingStatus) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.contact = contact;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.nights = nights;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
    }

    // ----- Getters -----
    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContact() {
        return contact;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    // ----- Setters (only the two things that can change after booking) -----
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    // Is this booking still active?
    public boolean isConfirmed() {
        return bookingStatus.equals(STATUS_CONFIRMED);
    }

    // Turns the booking into ONE line of text for the file.
    // Example: BK1001|Rahul Sharma|9876543210|102|Standard|3|4500.0|Paid|Confirmed
    public String toFileString() {
        return bookingId + "|" + customerName + "|" + contact + "|" + roomNumber + "|"
                + roomType + "|" + nights + "|" + totalAmount + "|"
                + paymentStatus + "|" + bookingStatus;
    }

    // Prints all booking details on the screen
    public void printDetails() {
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Customer Name  : " + customerName);
        System.out.println("Contact        : " + contact);
        System.out.println("Room Number    : " + roomNumber);
        System.out.println("Room Type      : " + roomType);
        System.out.println("Nights         : " + nights);
        System.out.println("Total Amount   : Rs. " + String.format("%.2f", totalAmount));
        System.out.println("Payment Status : " + paymentStatus);
        System.out.println("Booking Status : " + bookingStatus);
    }
}
