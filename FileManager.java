import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * FileManager.java
 * Does all the File I/O work: saving bookings to a text file and loading them back.
 * Each booking is stored as one line, fields separated by "|".
 */
public class FileManager {

    private String filePath;   // example: "data/bookings.txt"

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    // Writes ALL bookings to the file (the old file content is replaced)
    public void saveBookings(ArrayList<Booking> bookings) {
        File file = new File(filePath);

        // Create the "data" folder if it does not exist yet
        File folder = file.getParentFile();
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        // try-with-resources: the file is closed automatically at the end
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Booking booking : bookings) {
                writer.println(booking.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error: could not save bookings. " + e.getMessage());
        }
    }

    // Reads the file and returns a list of bookings.
    // If the file does not exist yet (first run), an empty list is returned.
    public ArrayList<Booking> loadBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return bookings;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;   // skip blank lines
                }
                Booking booking = parseLine(line);
                if (booking != null) {
                    bookings.add(booking);
                } else {
                    System.out.println("Warning: skipped a damaged line in the bookings file.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: could not read bookings file. " + e.getMessage());
        }

        return bookings;
    }

    // Converts ONE line of text back into a Booking object.
    // Returns null if the line is not in the correct format.
    private Booking parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 9) {
            return null;
        }

        try {
            String bookingId = parts[0];
            String customerName = parts[1];
            String contact = parts[2];
            int roomNumber = Integer.parseInt(parts[3]);
            String roomType = parts[4];
            int nights = Integer.parseInt(parts[5]);
            double totalAmount = Double.parseDouble(parts[6]);
            String paymentStatus = parts[7];
            String bookingStatus = parts[8];

            return new Booking(bookingId, customerName, contact, roomNumber,
                    roomType, nights, totalAmount, paymentStatus, bookingStatus);
        } catch (NumberFormatException e) {
            return null;   // a number in the line was not valid
        }
    }
}
