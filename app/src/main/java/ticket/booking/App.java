/*
 * This source file was generated by the Gradle 'init' task
 */
package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.utils.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to my Ticket Booking System!");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;

        try{
            userBookingService = new UserBookingService();
        }
        catch (IOException ex){
            System.out.println("There is something wrong!" + ex.getMessage());
            return;
        }

        Train trainSelectedForBooking = null;
        while(option!=7){
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option){
                case 1:
                    String signUpName;
                    while(true){
                        System.out.println("Enter your username to signup: ");
                        signUpName = scanner.nextLine();
                        if(signUpName.contains(" ")){
                            System.out.println("Username cannot contain spaces!");
                        }
                        else{
                            break;
                        }
                    }
                    System.out.println("Enter your password: ");
                    String signUpPass = scanner.nextLine();
                    User userToSignup = new User(signUpName, signUpPass, UserServiceUtil.hashPassword(signUpPass), new ArrayList<>(), UUID.randomUUID().toString());
                    try{
                        boolean userDuplicate = userBookingService.signUp(userToSignup);
                        if(userDuplicate){
                            System.out.println("Sign up successful!");
                        }
                        else{
                            break;
                        }
                    }catch (IOException ex){
                        System.out.println("Can SignUp User!");
                    }
                    break;
                case 2:
                    System.out.println("Enter the username to login: ");
                    String username;
                    while(true){
                        username = scanner.nextLine();
                        if(username.contains(" ")){
                            System.out.println("Username cannot contain spaces!");
                        }
                        else{
                            break;
                        }
                    }
                    System.out.println("Enter your password: ");
                    String password = scanner.nextLine();

                    Optional <User> foundUser = userBookingService.getUserByUsername(username);
                    if(foundUser.isPresent() && UserServiceUtil.checkPassword(password,foundUser.get().getHashedPassword())){
                        System.out.println("Login successful! Welcome " + username);
                        userBookingService.setUser(foundUser.get());
                    }
                    else{
                        System.out.println("Login failed!");
                    }
                    break;
                case 3:
                    try{
                        userBookingService.fetchBookings();
                    }catch (Exception ex){
                        System.out.println("There is something wrong!");
                    }
                    break;
                case 4:

                    // taking source and destination from user
                    System.out.println("Enter the source station: ");
                    String source = scanner.nextLine().toLowerCase(); // Converting to lowercase for consistency
                    System.out.println("Enter the destination station: ");
                    String destination = scanner.nextLine().toLowerCase(); //here also converting to lowercase

                    //this will fetch trains that are available
                    List<Train> trains = userBookingService.getTrains(source, destination);

                    // if trains list is empty means no trains available then it will exit
                    if (trains.isEmpty()) {
                        System.out.println("❌ No trains available between " + source + " and " + destination);
                        break;
                    }

                    // printing the available trains got from the above code
                    System.out.println("Available Trains:");
                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + ". Train ID: " + t.getTrainId() + " | Train No: " + t.getTrainNo());
                        System.out.println("   Route: " + String.join(" ➝ ", t.getStations()));
                        System.out.println("   Timings:");
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("     ⏰ " + entry.getKey() + " - " + entry.getValue());
                        }
                        index++;
                    }

                    System.out.println("Select a train by typing the corresponding number:");

                    int selectedIndex;
                    while (true) {
                        if (scanner.hasNextInt()) {
                            selectedIndex = scanner.nextInt();
                            scanner.nextLine(); // Consuming the newline character
                            if (selectedIndex > 0 && selectedIndex <= trains.size()) {
                                break;
                            } else {
                                System.out.println("❌ Invalid choice! Please select a valid train number.");
                            }
                        } else {
                            System.out.println("❌ Please enter a valid number.");
                            scanner.next(); // Clear invalid input
                        }
                    }

                    trainSelectedForBooking = trains.get(selectedIndex - 1); // Convert 1-based to 0-based index
                    System.out.println("✅ Train Selected: " + trainSelectedForBooking.getTrainId());

                    System.out.println("Available Seats:");
                    for (List<Integer> row : trainSelectedForBooking.getSeats()) {
                        for (Integer seat : row) {
                            System.out.print(seat + " ");
                        }
                        System.out.println();
                    }
                    break;

                case 5:

                    if (trainSelectedForBooking == null) {
                        System.out.println("❌ Please select a train first in option 4!");
                        break;
                    }
                    System.out.println("Select a seat out of these seats");

                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);

                    for (List<Integer> row: seats){
                        for (Integer val: row){
                            System.out.print(val +  " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Select the seat by typing the row and column");

                    System.out.println("Enter the row");
                    int row = scanner.nextInt();

                    System.out.println("Enter the column");
                    int col = scanner.nextInt();

                    System.out.println("Booking your seat....");

                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);

                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booked! Enjoy your journey");
                    }else{
                        System.out.println("Can't book this seat");
                    }
                    break;
                case 6:
                    System.out.println("Enter the ticket id to cancel the booking");
                    String ticketId = scanner.nextLine();
                    boolean isCancelled = userBookingService.cancelBooking(ticketId);

                    if(isCancelled){
                        System.out.println("Booking cancelled successfully!");
                    }
                    else{
                        System.out.println("Booking not found!");
                    }
                    break;
            }
        }


    }
}

