
public class Room {
	private int roomID;
	private String roomType;
	private String email;
	private double roomCost;
	private boolean balcony, lounge;

	/*
	 * This method saves all the argument variables to the global variables of the object
	 */
	public Room(int roomID, String roomType, double roomCost, boolean balcony, boolean lounge, String email){
		this.roomID = roomID;
		this.roomType = roomType;
		this.email = email;
		this.roomCost = roomCost;
		this.balcony = balcony;
		this.lounge = lounge;
	}
	
	
	public String getEmail(){
		return email;
	}
	
	//Returns a formatted version of all the variable data for printing to screen
	public String toString(){
		String result = roomID + " " + roomType + " " + roomCost + " " + balcony + " " + lounge + " " + email;
		return result;
	}
	
	//Checks if the user inputted ID is correct and if the room is unreserved
	public void reserve(int roomID, String email){
		if(this.roomID == roomID && this.email == ""){
			this.email = email;
		}
	}
	
	//Checks if all the user inputted data matches the data stored in the room object
	public int search(String roomType, Double lowerRoomCost, Double higherRoomCost, Boolean balcony, Boolean lounge, int matches, boolean bestMatch){
		int noMatches=0;
		
		if((this.roomType.toLowerCase().equals(roomType) || roomType.equals("all")) && ((this.roomCost >= lowerRoomCost &&  this.roomCost <= higherRoomCost) || (higherRoomCost == null && lowerRoomCost == null))  && (this.balcony == balcony || balcony == null) && (this.lounge == lounge || lounge == null) && this.email == "" && bestMatch == false){
			System.out.println(toString());
			matches++;
		}else if(bestMatch == true && this.email == ""){	//This section of code is if the normal search produced no results then a best match statement is ran.
				if(this.roomType.toLowerCase().equals(roomType)){
					noMatches++;
				}
		
				if(this.roomCost >= lowerRoomCost && this.roomCost <= higherRoomCost){
				}
		
				if(this.balcony == balcony){
					noMatches++;
				}
		
				if(this.lounge == lounge){
					noMatches++;
				}
			return noMatches;	
		}
		return matches;
	}
	
	//This function will find if the email is the same as the one user inputted and will set it to an empty string.
	public void cancel(String email){
		if(this.email.toLowerCase().contains(email)){
			this.email = "";
		}
	}
}
