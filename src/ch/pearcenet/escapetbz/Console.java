package ch.pearcenet.escapetbz;

import java.util.Scanner;

public class Console {
	
	// GLOBAL CONSTANTS
	public static final String VERSION = "v1.0";
	protected static final Scanner input = new Scanner(System.in);
	
	// GLOBAL VARIABLES
	public static Player[] players = new Player[5];
	
	public static void main(String[] args) {
		
		// Display opening screen and options
		printOpening();
		System.out.print("Enter your username: ");
		Player[] players =
		
	}
	
	// Prints out opening info
	private static void printOpening() {
		System.out.println(""
				+ "\n   ESCAPE FROM TBZ"
				+ "\n"
				+ "\n   by   Amin Haidar"
				+ "\n    & Samuel Pearce"
				+ "\n"
				+ "\n Version " + VERSION
				+ "\n"
				);
	}

}
