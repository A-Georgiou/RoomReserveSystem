import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelRoomSystem {
	private static Scanner sc = new Scanner(System.in); // Declare the scanner and make it static so it can be referenced
	private static final ArrayList<Room> rooms = new ArrayList<Room>();

	public static void main(String[] args) throws IOException, InterruptedException {
		loadRooms();
		MainMenu();
	}

	/*
	 * This is the first method to run and it will load all the variables line by line from the text file, split them up and create new room objects for each one
	 */
	public static void loadRooms() throws FileNotFoundException {
		Scanner fileScan = new Scanner(new FileReader("M:\\data\\rooms.txt"));
		int roomID;
		String fileRoomType;
		Double fileRoomCost;
		boolean fileBalcony, fileLounge;

		while (fileScan.hasNext()) {
			String fileEmail = "";

			String line = fileScan.nextLine();
			String lineSplit[] = line.split(" ");

			if (lineSplit.length > 4) {
				roomID = Integer.parseInt(lineSplit[0]);
				fileRoomType = lineSplit[1];
				fileRoomCost = Double.parseDouble(lineSplit[2]);
				fileBalcony = Boolean.parseBoolean(lineSplit[3]);
				fileLounge = Boolean.parseBoolean(lineSplit[4]);
				if (lineSplit.length > 5) {
					fileEmail = lineSplit[5];
				}
				rooms.add(new Room(roomID, fileRoomType, fileRoomCost, fileBalcony, fileLounge, fileEmail));
			}
		}
		fileScan.close();
	}

	/*
	 * This method will be used to display all the rooms
	 */
	public static void displayReservedRooms() {
		for (int i = 0; i < rooms.size(); i++) {
			if (!rooms.get(i).getEmail().equals("")) {
				System.out.println(rooms.get(i).toString());
			}
		}
		return;
	}

	/*
	 * This is the main menu method, I utilise this method through recursion
	 */
	public static void MainMenu() throws IOException, InterruptedException {
		System.out.print("-- Room Booking --\n\n -- Main Menu --\n1- Reserve Room\n2- Cancel Room\n3- View Room Reservations\nQ- Quit\nPick: "); // The Header for the program output
		String input = sc.next().toLowerCase();
		switch (input) {
		case "1": {
			searchRooms();
			break;
		}
		case "2": {
			cancelRoom();
			break;
		}
		case "3": {
			RoomReservations();
			break;
		}
		case "q": {
			System.exit(0);
		}
		default: {
			System.out.println("Incorrect input entered, try again. \n");
			MainMenu();
		}
		}
	}

	/*
	 * This method is called upon to write a string containing the whole file to the file.
	 */
	@SuppressWarnings("resource")
	public static void writeFile(String wholeFile) throws IOException {
		String fileLoc = "M:\\data\\rooms.txt";
		PrintWriter fileWrite = new PrintWriter(new FileWriter(fileLoc, true));
		new PrintWriter(fileLoc);
		fileWrite.write(wholeFile);
		fileWrite.flush();
		fileWrite.close();
	}

	/*
	 * The search function gets the users search queries, searches for matching rooms, if it doesnt find one it will run through to find the best match
	 */
	public static void searchRooms() throws IOException, InterruptedException {
		Boolean balconyBoolean = false, loungeBoolean = false;
		int matches = 0, noMatches = 0;
		boolean bestMatch = false;

		System.out.println("\n --Search Rooms--");
		System.out.print("Room Type (Single/Double/Suite/All): ");
		String roomType = sc.next().toLowerCase();
		System.out.print("Lowest Price: £");
		Double roomLowerCost = sc.nextDouble();
		System.out.print("Highest Price: £");
		Double roomHigherCost = sc.nextDouble();
		System.out.print("Would you like a balcony (Y/N): ");
		String balconyString = sc.next().toLowerCase();
		System.out.print("Would you like a lounge (Y/N): ");
		String loungeString = sc.next().toLowerCase();
		
		//This section of code is validation
		
		if (balconyString.equals("y")) {
			balconyBoolean = true;
		}else if(balconyString.equals("n")){
			balconyBoolean = false;
		}else{
			System.out.println("Invalid balcony input");
			searchRooms();
		}

		if (loungeString.equals("y")) {
			loungeBoolean = true;
		}else if(loungeString.equals("n")){
			loungeBoolean = false;
		}else{
			System.out.println("Invalid lounge input");
			searchRooms();
		}

		System.out.println("\nSearching...");

		for (int i = 0; i < rooms.size(); i++) {
			matches = rooms.get(i).search(roomType, roomLowerCost, roomHigherCost, balconyBoolean, loungeBoolean, matches, bestMatch);
		}

		if (matches > 0) {
			System.out.println("\n" + matches + " matches found!");
			reserveRoom();
		} else {
			bestMatch = true;
			int tempNoMatches = 0;
			int currentRoom = 0;
			int maxMatchValue = 0;

			for (int i = 0; i < rooms.size(); i++) {
				tempNoMatches = maxMatchValue;
				noMatches = rooms.get(i).search(roomType, roomLowerCost, roomHigherCost, balconyBoolean, loungeBoolean, matches, bestMatch);
				
				if (noMatches > tempNoMatches) {
					currentRoom = i;
					maxMatchValue = noMatches;
				}
			}

			if (maxMatchValue > 0) {
				System.out.println("No matches found, we can match " + maxMatchValue + " of your requirements:");
				System.out.println(rooms.get(currentRoom).toString());
				MainMenu();
			} else {
				searchRooms();
			}
		}
	}

	/*
	 * The reserve room function simply gets the room that matches the id the user enters and inserts the users email to the object before writing to the file
	 */
	public static void reserveRoom() throws IOException, InterruptedException {
		int roomID = 0;
		
		System.out.println("\n --Reserve Room--");
		System.out.print("Please enter your email address: ");
		String email = sc.next();
		
		if(email.contains("@")){
			System.out.println("Incorrect email, please try again: ");
			reserveRoom();
		}
		
		System.out.print("Please enter the ID of the room you wish to reserve: ");
		
		try{
			roomID = sc.nextInt();
		}catch(Exception e){
			System.out.println("Incorrect value entered, please try again");
			reserveRoom();
		}
		
		String wholeFile = "";

		for (int i = 0; i < rooms.size(); i++) {
			rooms.get(i).reserve(roomID, email);
			wholeFile += rooms.get(i).toString() + System.getProperty("line.separator");
		}

		writeFile(wholeFile);

		System.out.println("Room successfully reserved!");
		System.out.println("-------------------------------------------------------\n");
		MainMenu();
	}

	/*
	 * The cancel room function simply finds the room object with an email matching the users email and sets the email to a blank string before writing to the file
	 */
	public static void cancelRoom() throws IOException, InterruptedException {
		String wholeFile = "";

		System.out.println("\n --Cancel Room--");
		System.out.print("\nEnter your email address: ");
		String email = sc.next().toLowerCase();

		if(!email.contains("@")){
			System.out.println("Invalid email entered, please try again");
			cancelRoom();
		}
		
		for (int i = 0; i < rooms.size(); i++) {
			rooms.get(i).cancel(email.toLowerCase());
			wholeFile += rooms.get(i).toString() + System.getProperty("line.separator"); // Stores basically the whole text document in a string but includes any changes (line separator adds a new line)
		}

		writeFile(wholeFile);

		System.out.println("Room successfully cancelled. Have a nice day!");
		System.out.println("-------------------------------------------------------\n");
		MainMenu();

	}

	/*
	 * This method simply displays all the reserved rooms (its more just good design with print statements)
	 */
	public static void RoomReservations() throws IOException, InterruptedException {
		System.out.println("\n --View Reserved Rooms--\n");
		System.out.println("ID | Type | Cost | Balcony | Lounge | Reserved user");
		displayReservedRooms();
		System.out.println("\nOperation Completed!");
		System.out.println("-------------------------------------------------------\n");
		MainMenu();
	}
}