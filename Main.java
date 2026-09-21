import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main.java
 * Starts the program, shows the menus and reads what the user types.
 * All the real work (rooms, bookings, files) is done by the Hotel class.
 */
public class Main {

    private static final int MAX_NIGHTS = 30;

    private static Scanner scanner = new Scanner(System.in);
    private static Hotel hotel;

    public static void main(String[] args) {
        FileManager fileManager = new FileManager("data/bookings.txt");
        hotel = new Hotel("Grand Palace Hotel", fileManager);

        try {
            runMenu();
        } catch (NoSuchElementException e) {
            // Happens only if the input stream is closed (for example Ctrl+Z on Windows)
            System.out.println("\nInput closed. Exiting program.");
        }
    }

    // ---------- Main menu loop ----------

    private static void runMenu() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("========== HOTEL RESERVATION SYSTEM ==========");
            System.out.println("            " + hotel.getName());
            System.out.println("----------------------------------------------");
            System.out.println("1. View/Search available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. View booking details");
            System.out.println("4. Cancel booking");
            System.out.println("5. View all rooms");
            System.out.println("6. Exit");
            System.out.println("----------------------------------------------");

            int choice = readIntInRange("Enter your choice (1-6): ", 1, 6);

            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    viewBookingDetails();
                    break;
                case 4:
                    cancelBooking();
                    break;
                case 5:
                    printRoomTable(hotel.getAllRooms(), "ALL ROOMS");
                    break;
                case 6:
                    System.out.println("\nThank you for using the Hotel Reservation System. Goodbye!");
                    running = false;
                    break;
            }
        }
    }

    // ---------- 1. Search rooms ----------

    private static void searchRooms() {
        System.out.println("\n---------- SEARCH ROOMS ----------");
        System.out.println("1. Show all available rooms");
        System.out.println("2. Search by category");
        System.out.println("3. Search available rooms by category");
        System.out.println("4. Back to main menu");

        int choice = readIntInRange("Enter your choice (1-4): ", 1, 4);

        if (choice == 1) {
            printRoomTable(hotel.getAvailableRooms(), "AVAILABLE ROOMS");
        } else if (choice == 2) {
            String category = chooseCategory();
            printRoomTable(hotel.getRoomsByCategory(category), category.toUpperCase() + " ROOMS");
        } else if (choice == 3) {
            String category = chooseCategory();
            printRoomTable(hotel.getAvailableRoomsByCategory(category),
                    "AVAILABLE " + category.toUpperCase() + " ROOMS");
        }
        // choice 4: do nothing, just go back
    }

    // Asks the user to pick Standard / Deluxe / Suite and returns the name
    private static String chooseCategory() {
        System.out.println("\nSelect category:");
        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");

        int choice = readIntInRange("Enter your choice (1-3): ", 1, 3);

        if (choice == 1) {
            return "Standard";
        } else if (choice == 2) {
            return "Deluxe";
        } else {
            return "Suite";
        }
    }

    // Prints a list of rooms as a table
    private static void printRoomTable(ArrayList<Room> roomList, String title) {
        System.out.println("\n---------- " + title + " ----------");

        if (roomList.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }

        System.out.printf("%-8s %-10s %-16s %-10s%n", "Room", "Category", "Price/Night", "Status");
        System.out.println("------------------------------------------------");
        for (Room room : roomList) {
            System.out.printf("%-8d %-10s %-16s %-10s%n",
                    room.getRoomNumber(),
                    room.getCategory(),
                    "Rs. " + String.format("%.2f", room.getPricePerNight()),
                    room.getStatusText());
        }
    }

    // ---------- 2. Book a room ----------

    private static void bookRoom() {
        System.out.println("\n---------- BOOK A ROOM ----------");

        ArrayList<Room> availableRooms = hotel.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("Sorry, no rooms are available right now.");
            return;
        }

        printRoomTable(availableRooms, "AVAILABLE ROOMS");

        // Step 1: choose a valid, available room (0 = go back)
        Room room = null;
        while (room == null) {
            int roomNumber = readInt("\nEnter room number to book (0 to go back): ");

            if (roomNumber == 0) {
                System.out.println("Booking cancelled.");
                return;
            }

            Room foundRoom = hotel.findRoom(roomNumber);
            if (foundRoom == null) {
                System.out.println("Invalid room number. Please choose a room from the list.");
            } else if (!foundRoom.isAvailable()) {
                System.out.println("Room " + roomNumber + " is already booked. Please choose another room.");
            } else {
                room = foundRoom;
            }
        }

        // Step 2: customer details
        String customerName = readNonEmptyText("Enter customer name: ");
        String contact = readNonEmptyText("Enter contact information (phone/email): ");
        int nights = readNights();

        double total = room.calculateTotal(nights);

        // Step 3: booking summary + confirmation
        System.out.println("\n---------- BOOKING SUMMARY ----------");
        System.out.println("Customer   : " + customerName);
        System.out.println("Contact    : " + contact);
        System.out.println("Room       : " + room.getRoomNumber() + " (" + room.getCategory() + ")");
        System.out.println("Price/Night: Rs. " + String.format("%.2f", room.getPricePerNight()));
        System.out.println("Nights     : " + nights);
        System.out.println("Total      : Rs. " + String.format("%.2f", total));
        System.out.println("\nConfirm this booking?");
        System.out.println("1. Yes");
        System.out.println("2. No");

        int confirm = readIntInRange("Enter your choice (1-2): ", 1, 2);
        if (confirm == 2) {
            System.out.println("Booking not confirmed. Returning to main menu.");
            return;
        }

        // Step 4: payment simulation (no real payment)
        System.out.println("\n---------- PAYMENT SUMMARY ----------");
        System.out.println("Room: " + room.getRoomNumber());
        System.out.println("Nights: " + nights);
        System.out.println("Total: Rs. " + String.format("%.2f", total));
        System.out.println("\n1. Pay");
        System.out.println("2. Cancel");

        int payChoice = readIntInRange("Enter your choice (1-2): ", 1, 2);
        if (payChoice == 2) {
            System.out.println("Payment cancelled. The booking was not created.");
            return;
        }

        System.out.println("\nPayment successful.");

        // Step 5: create the booking (this also marks the room as booked and saves the file)
        Booking booking = hotel.createBooking(room, customerName, contact, nights);

        System.out.println("\n---------- BOOKING CONFIRMED ----------");
        booking.printDetails();
        System.out.println("\nPlease note your Booking ID: " + booking.getBookingId());
    }

    // Asks for the number of nights until a valid value (1 to MAX_NIGHTS) is entered
    private static int readNights() {
        while (true) {
            int nights = readInt("Enter number of nights (1-" + MAX_NIGHTS + "): ");
            if (nights >= 1 && nights <= MAX_NIGHTS) {
                return nights;
            }
            System.out.println("Invalid number of nights. Please enter a value from 1 to " + MAX_NIGHTS + ".");
        }
    }

    // ---------- 3. View booking details ----------

    private static void viewBookingDetails() {
        System.out.println("\n---------- BOOKING DETAILS ----------");
        String bookingId = readNonEmptyText("Enter booking ID: ");

        Booking booking = hotel.findBooking(bookingId);
        if (booking == null) {
            System.out.println("No booking found with ID " + bookingId + ".");
            return;
        }

        System.out.println();
        booking.printDetails();
    }

    // ---------- 4. Cancel booking ----------

    private static void cancelBooking() {
        System.out.println("\n---------- CANCEL BOOKING ----------");
        String bookingId = readNonEmptyText("Enter booking ID to cancel: ");

        Booking booking = hotel.findBooking(bookingId);
        if (booking == null) {
            System.out.println("No booking found with ID " + bookingId + ".");
            return;
        }

        System.out.println();
        booking.printDetails();

        if (!booking.isConfirmed()) {
            System.out.println("\nThis booking is already cancelled.");
            return;
        }

        System.out.println("\nAre you sure you want to cancel this booking?");
        System.out.println("1. Yes, cancel it");
        System.out.println("2. No, keep it");

        int confirm = readIntInRange("Enter your choice (1-2): ", 1, 2);
        if (confirm == 2) {
            System.out.println("Cancellation aborted. Your booking is still active.");
            return;
        }

        boolean cancelled = hotel.cancelBooking(booking);
        if (cancelled) {
            System.out.println("\nBooking " + booking.getBookingId() + " has been cancelled.");
            System.out.println("Payment of Rs. " + String.format("%.2f", booking.getTotalAmount())
                    + " will be refunded (simulated).");
            System.out.println("Room " + booking.getRoomNumber() + " is available again.");
        }
    }

    // ---------- Input helper methods ----------

    // Reads a whole number. Keeps asking until the user types a valid integer.
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // Reads a whole number that must be between min and max (inclusive)
    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Invalid choice. Please enter a number from " + min + " to " + max + ".");
        }
    }

    // Reads text that is not empty. The "|" character is not allowed
    // because it is used to separate the fields in the bookings file.
    private static String readNonEmptyText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            } else if (input.contains("|")) {
                System.out.println("The character '|' is not allowed. Please try again.");
            } else {
                return input;
            }
        }
    }
}
