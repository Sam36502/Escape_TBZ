package ch.pearcenet.escapetbz;

import ch.pearcenet.tui.input.Input;

public class Console {
	
	// GLOBAL CONSTANTS
	public static final String VERSION = "v1.0";
	public static final int MAX_FLOORS = 25;
	public static final int MAX_PLAYERS = 4;
	public static final int MAX_INVENTORY = 10;
	public static final int MAX_ROOMS_PER_FLOOR = 50;
	public static final int MAX_ITEMS_PER_ROOM = 5;
	public static final int MAX_MOVES_PER_TURN = 5;
	public static final int MAX_INTERACTABLE_PER_ROOM = 10;
	
	// GLOBAL VARIABLES
	public static Player[] players;
	
	public static void main(String[] args) {
		
		/// INTRODUCTION ///
		
		// Display opening screen
		printOpening();
		Input.openScanner();
		System.out.println("How many players are playing? (max. " + MAX_PLAYERS + ")");
		int numPlayers = Input.getInt(1, MAX_PLAYERS);
		players = new Player[numPlayers];
		
		// Set up each player
		for (int i=0; i<numPlayers; i++) {
			System.out.println("Player " + i + ", What is your name?");
			String name = Input.getString();
			players[i] = new Player(name);
		}
		
		/// MAIN LOOP ///
		
		// Loop through each round until someone wins
		boolean isRunning = true;
		while (isRunning) {
			
			// Loop through each player for their turn
			for (Player currentPlayer: players) {
				
				// Give each player MAX_MOVES_PER_TURN moves
				for (int mv=1; mv<=MAX_MOVES_PER_TURN; mv++) {
					
					//
					
				}
				
			}
			
		}
		
		/// TERMINATION ///
		
		// Close the input scanner
		Input.closeScanner();
		
	}
	
	// Prints out opening info
	private static void printOpening() {
		System.out.println(""
				+ "\n   ESCAPE FROM TBZ"
				+ "\n  -----------------"
				+ "\n"
				+ "\n   by   Amin Haidar"
				+ "\n    & Samuel Pearce"
				+ "\n"
				+ "\n Version " + VERSION
				+ "\n"
				);
	}

}
