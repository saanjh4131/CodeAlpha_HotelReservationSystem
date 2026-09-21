# Hotel Reservation System (Java Console Application)

## Description
A simple console-based hotel reservation system written in Java. Users can search rooms by category and availability, book a room, simulate a payment, view booking details and cancel a booking. All bookings are saved in a text file, so nothing is lost when the program is closed.

This project was built as **Task 4** of a Java internship, using only core Java (OOP, `ArrayList`, `Scanner` and File I/O).

## Features
- Main menu with 6 options
- 8 default rooms in 3 categories: **Standard**, **Deluxe**, **Suite**
- Search rooms by category, by availability, or both
- Book a room (customer name, contact, number of nights, automatic total price)
- Unique booking IDs (`BK1001`, `BK1002`, ...)
- Payment simulation (no real payment gateway)
- View booking details using a booking ID
- Cancel a booking (with confirmation); the room becomes available again
- Bookings and room availability are saved to `data/bookings.txt` and loaded at startup
- Input validation: invalid menu choices, invalid numbers, invalid room numbers, invalid nights, unavailable rooms, unknown booking IDs, empty name/contact

## Technologies / Concepts Used
- Java (JDK 17 or newer)
- Object-Oriented Programming: classes, objects, constructors, private fields, getters/setters, encapsulation
- `ArrayList` and loops
- `Scanner` for keyboard input
- File I/O: `FileReader`, `BufferedReader`, `FileWriter`, `PrintWriter`
- Exception handling (`try-catch`, `NumberFormatException`, `IOException`)

## Project Structure
```
HotelReservationSystem/
|
|-- src/
|   |-- Main.java          (menu and user input)
|   |-- Room.java          (one hotel room)
|   |-- Booking.java       (one reservation)
|   |-- Hotel.java         (rooms, bookings, main logic)
|   `-- FileManager.java   (saving/loading bookings)
|
|-- data/
|   `-- bookings.txt       (saved bookings)
|
`-- README.md
```

## How to Run
**In VS Code (Windows)**
1. Install a JDK (17 or 21) and the *Extension Pack for Java* in VS Code.
2. `File > Open Folder...` and select the `HotelReservationSystem` folder.
3. Open `src/Main.java` and click **Run** above the `main` method.

**From the terminal (inside the `HotelReservationSystem` folder)**
```
javac -d bin src\*.java
java -cp bin Main
```
> Always run the program from the project folder, so that `data/bookings.txt` is created/read in the right place.

## Sample Functionality
```
========== HOTEL RESERVATION SYSTEM ==========
1. View/Search available rooms
2. Book a room
3. View booking details
4. Cancel booking
5. View all rooms
6. Exit

---------- PAYMENT SUMMARY ----------
Room: 102
Nights: 3
Total: Rs. 4500.00

1. Pay
2. Cancel

Payment successful.

Booking ID     : BK1001
Customer Name  : Rahul Sharma
Room Number    : 102
Total Amount   : Rs. 4500.00
Payment Status : Paid
Booking Status : Confirmed
```

Each booking is stored as one line in `data/bookings.txt`:
```
BK1001|Rahul Sharma|9876543210|102|Standard|3|4500.0|Paid|Confirmed
```

## Future Improvements
- Check-in and check-out dates (so a room can be booked for different dates)
- Admin menu to add, edit or remove rooms
- Search rooms by price range
- Store data in a real database (MySQL/SQLite with JDBC)
- Simple login for staff and customers
- A GUI (JavaFX/Swing) or a web version
