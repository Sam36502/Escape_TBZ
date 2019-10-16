package ch.pearcenet.escapetbz;

import java.io.File;

import ch.pearcenet.tui.input.Input;
import ch.pearcenet.tui.output.ArrayOutput;

public class Console {
	
	// GLOBAL CONSTANTS
	public static final String VERSION = "v1.0";
	public static final int MAX_FLOORS = 25;
	public static final int MAX_PLAYERS = 4;
	public static final int MAX_INVENTORY = 10;
	public static final int HUNGER_PER_ACTION = 1;
	public static final int THIRST_PER_ACTION = 2;
	public static final int MAX_ITEMS_PER_ROOM = 5;
	public static final int MAX_MOVES_PER_TURN = 5;
	public static final int MAX_ROOMS_PER_FLOOR = 50;
	public static final int MAX_INTERACTABLE_PER_ROOM = 10;
	
	public static void main(String[] args) {
		
		/// SETUP ///
		
		// Get world folder from arguments
		File worldFolder = null;
		if (args.length == 1) {
			worldFolder = new File(args[0]);
			if (!worldFolder.exists() || !worldFolder.isDirectory()) {
				System.out.println("Please specify a valid folder path to load the levels from.");
				System.exit(0);
			}
		} else {
			System.out.println("Please specify a valid folder path to load the levels from.");
			System.exit(0);
		}
		
		// Load levels
		System.out.println("Loading levels from '" + worldFolder.getName() + "'...");
		Level[] levels = new Level[MAX_FLOORS];
		String path = worldFolder.getAbsolutePath();
		int loadedLevels = 0;
		
		for (int levelNum=0; levelNum<MAX_FLOORS; levelNum++) {
			File tmp = new File(path + "/level_" + (levelNum + 1) + ".txt");
			
			if (tmp.exists()) {
				loadedLevels++;
				levels[levelNum] = new Level(levelNum + 1);
				levels[levelNum].loadMap(tmp.getAbsolutePath());
			}
			
		}
	
		System.out.println("Done! Loaded " + loadedLevels + " levels.\n");
		
		// Determine highest numbered level
		int firstLevelIndex = MAX_FLOORS-1;
		for (Level curr: levels) if (curr == null) firstLevelIndex--;
		
		
		/// INTRODUCTION ///
		
		// Display opening screen
		System.out.println(getOpening());
		Input.openScanner();
		Input.setPrompt(" > ");
		System.out.println("How many players are playing? (max. " + MAX_PLAYERS + ")");
		int numPlayers = Input.getInt(1, MAX_PLAYERS);
		Player[] players = new Player[numPlayers];
		Player[] waiting = new Player[numPlayers];
		
		// Make leaderboard for each level
		Player[] leaders = new Player[numPlayers];
		int lastLeader = 0;
		
		// Set up each player
		for (int i=0; i<numPlayers; i++) {
			System.out.println("Player " + (i+1) + ", What is your name?");
			String name = Input.getString();
			players[i] = new Player(name);
		}
		
		/// MAIN LOOP ///
		
		// Loop through each round until someone wins
		for (int lvlNum=firstLevelIndex; lvlNum>=0; lvlNum--) {
			Level currentLevel = levels[lvlNum];
			
			// Skip empty levels
			if (currentLevel == null) {
				continue;
			}
			
			// Put all the players into the level
			for (int i=0; i<numPlayers; i++) players[i].setRoom(currentLevel.getStart());
			
			System.out.println("\nLevel " + currentLevel.getNumber() + ":");
			boolean levelFin = false;
			while (!levelFin) {
			
				// Loop through each player for their turn
				for (int playerIndex=0; playerIndex<numPlayers; playerIndex++) {
					Player currentPlayer = players[playerIndex];
					
					// Skip waiting players
					if (currentPlayer == null) {
						continue;
					}
					
					// Give each player MAX_MOVES_PER_TURN moves
					System.out.println("\n-----------------------------------"
									+ "\n It's " + currentPlayer + "'s turn"
									+ "\n-----------------------------------");
					for (int mv=1; mv<=MAX_MOVES_PER_TURN; mv++) {
						
						// Prompt the user for their input
						String result = promptUser(currentPlayer);
						
						// Check if player reached exit
						if (currentLevel.getEnd().equals(currentPlayer.getRoom())) {
							System.out.println("Player " + currentPlayer + " has finished the level!\n");
							
							// Remove player from the playing pool
							waiting[playerIndex] = currentPlayer;
							leaders[lastLeader++] = currentPlayer;
							players[playerIndex] = null;
							
							// Check if all players are done
							levelFin = true;
							for (Player p: players) {
								if (p != null) levelFin = false;
							}
							
							break;
						}
						
						// Check the noturn flag
						if (result.length() >= 8 &&"%noturn%".equals(result.substring(result.length() - 8))) {
							mv--;
							result = result.substring(0, result.length() - 8);
						}
						
						// Check the endturn flag
						if (result.length() >= 9 &&"%endturn%".equals(result.substring(result.length() - 9))) {
							break;
						}
						
						// Output result
						System.out.println(result);
						
					}
					
					// If Level is finished, exit loop
					if (levelFin) break;
					
				}
				
			}
			
			// Display level statistics and reset leaderboard
			System.out.println(" Level " + currentLevel.getNumber() + " Stats:\n----------------");
			for (int i=0; i<numPlayers; i++) {
				System.out.println(" Rank " + (i+1) + ": " + leaders[i].getName());
				players[i] = waiting[i];
				waiting[i] = null;
				leaders[i] = null;
			}
			lastLeader = 0;
		
		}
		
		/// TERMINATION ///
		
		System.out.println("\n--------------------\n  GAME FINISHED  \n------------------\n\nThanks for playing!");
		
		// Close the input scanner
		Input.closeScanner();
		
	}
	
	/// PRIVATE METHODS ///
	
	// Prompts the user for input and then parses the command
	public static String promptUser(Player player) {
		
		// Output player's stats
		System.out.println("\n" + player + "'s Stats:"
						+ "\n" + "-".repeat(player.getName().length()) + "----------"
						+ "\n Hunger: " + player.getHunger() + ""
						+ "\n Thirst: " + player.getThirst() + ""
						+ "\n" + "-".repeat(player.getName().length()) + "----------");
		
		// Get user input and parse it
		String command = Input.getString();
		
		// Split cmd from front of command
		command = command.strip();
		
		String cmd = null;
		String arg1 = null;
		String arg2 = null;
		
		int sidx = command.indexOf(' ');
		if (sidx != -1) {
			cmd = command.substring(0, sidx).toLowerCase();
		} else {
			cmd = command.toLowerCase();
		}
		command = command.substring(sidx + 1);
		
		// Split Arguments
		int oidx = command.toLowerCase().indexOf(" on ");
		
		if (oidx != -1) {
			arg1 = command.substring(0, oidx);
			arg2 = command.substring(oidx + 4);
		} else if (sidx != -1) {
			arg1 = command;
		}
		
		// Parse the command
		switch(cmd) {
		
		case "help":
		case "?":
			if (arg1 == null) {
				return getHelpMenu() + "%noturn%";
			} else {
				return getHelp(arg1) + "%noturn%";
			}
		
		case "move":
		case "mv":
		case "go":
			if (arg1 != null) {
				Room.Direction dir = Room.parseDirection(arg1);
				if (dir == null) return "\nInvalid direction '" + arg1 + "'.";
				return player.move(dir);
			} else {
				return "\nThe 'move' command requires 1 argument.%noturn%";
			}
			
		case "pick-up":
		case "pickup":
		case "take":
		case "pu":
			if (arg1 != null) {
				return player.pickup(arg1);
			} else {
				return "\nThe 'pick-up' command requires 1 argument.%noturn%";
			}
			
		case "look":
		case "lk":
			if (arg1 == null) {
				return player.look();
			} else {
				return "\nThe 'look' command requires no arguments.%noturn%";
			}
			
		case "consume":	
		case "drink":
		case "eat":
		case "cs":
			if (arg1 != null) {
				return player.consume(new Item(arg1));
			} else {
				return "\nThe 'consume' command requires 1 argument.%noturn%";
			}
			
		case "use":
			if (arg1 != null && arg2 != null) {
				Item key = player.findItem(arg1);
				Interactable inter = player.getRoom().findInteractable(arg2);
				
				if (key != null && inter != null) {
					return inter.useWith(key);
				} else if (key == null) {
					return "\nYou don't have a '" + arg1 + "' in your inventory.";
				} else if (inter == null) {
					return "\nYou can't see a '" + arg2 + "' in the room";
				}
			} else {
				return "\nThe 'use on' command requires 2 arguments.%noturn%";
			}
			
		case "interact-with":
		case "in":
			if (arg1 != null) {
				Interactable inter = player.getRoom().findInteractable(arg1);
				if (inter != null) {
					return inter.interact();
				} else {
					return "\nYou can't see a '" + arg1 + "' in the room";
				}
			} else {
				return "\nThe 'interact-with' command requires 1 argument.%noturn%";
			}
			
		case "inventory":
		case "items":
		case "iv":
			if (arg1 == null) {
				return "\n" + player + "'s Items:\n" + ArrayOutput.sentenceArray(player.getInventory().toArray()) + "%noturn%";
			} else {
				return "\nThe 'inventory' command requires no arguments.%noturn%";
			}
			
		case "end-turn":
		case "wait":
		case "wt":
			if (arg1 == null) {
				return "\n" + player + " Has ended their turn.%endturn%";
			} else {
				return "\nThe 'wait' command requires no arguments.%noturn%";
			}
			
		}
		
		return "\nInvalid command.%noturn%";
		
	}
	
	// Returns the help menu
	public static String getHelpMenu() {
		return ""
				+ "\n  ### Help Menu ### "
				+ "\n -------------------------------"
				+ "\n  use 'help cmdname' for more"
				+ "\n  information about any command"
				+ "\n -------------------------------"
				+ "\n  help/? [command]"
				+ "\n  move/mv/go <direction>"
				+ "\n  pickup/pick-up/take/pu <item>"
				+ "\n  look/lk [object/item]"
				+ "\n  eat/drink/consume/cs <item>"
				+ "\n  use <item> with <object>"
				+ "\n  interact-with/in <object>"
				+ "\n  inventory/items/iv"
				+ "\n  end-turn/wait/wt"
				+ "\n -------------------------------"
				;
	}
	
	// Returns a help page for each command
	public static String getHelp(String cmd) {
		switch(cmd) {
		
		case "help":
		case "?":
			return ""
					+ "\nHELP COMMAND:"
					+ "\n-------------"
					+ "\nUsage: help/? [command]"
					+ "\n"
					+ "\n This command without any arguments displays a"
					+ "\n list of all possible commands."
					+ "\n"
					+ "\n When passed a command as an argument, this will"
					+ "\n display information about that command."
					+ "\n"
					+ "\nExamples:"
					+ "\n help"
					+ "\n ? pickup"
					+ "\n help pick-up"
					;
		
		case "move":
		case "mv":
		case "go":
			return ""
					+ "\nMOVE COMMAND:"
					+ "\n-------------"
					+ "\nUsage: move/mv/go <direction>"
					+ "\n"
					+ "\n This command will move the player in the"
					+ "\n direction provided, and requires one argument."
					+ "\n If the player cannot move in the given direction,"
					+ "\n they will be told why not."
					+ "\n"
					+ "\nExamples:"
					+ "\n move north"
					+ "\n go NoRtH"
					+ "\n mv EAST"
					;
			
		case "pick-up":
		case "pickup":
		case "take":
		case "pu":
			return ""
					+ "\nPICK UP COMMAND:"
					+ "\n----------------"
					+ "\nUsage: pick-up/pickup/pu <item>"
					+ "\n"
					+ "\n This command lets the player pick up an item"
					+ "\n if it's in the same room as them. This command"
					+ "\n requires one argument. For a list of Items in"
					+ "\n the current room, use 'look'"
					+ "\n"
					+ "\nExamples:"
					+ "\n pick-up Water Bottle"
					+ "\n pickup Burger"
					+ "\n pu Master Key"
					;
			
		case "look":
		case "lk":
			return ""
					+ "\nLOOK COMMAND:"
					+ "\n-------------"
					+ "\nUsage: look/lk"
					+ "\n"
					+ "\n This command gives you a description of the"
					+ "\n room your player is in. This is the same"
					+ "\n description you get when you walk into a room."
					+ "\n This includes listing all items, objects and"
					+ "\n paths in the room."
					+ "\n"
					+ "\nExamples:"
					+ "\n look"
					+ "\n lk"
					;
			
		case "consume":	
		case "drink":
		case "eat":
		case "cs":
			return ""
					+ "\nCONSUME COMMAND:"
					+ "\n----------------"
					+ "\nUsage: consume/drink/eat/cs <item>"
					+ "\n"
					+ "\n This command allows the user to replenish"
					+ "\n their depleted 'hunger' and 'thirst' meters."
					+ "\n The command requires the name of a food item"
					+ "\n the user has in their inventory as an argument."
					+ "\n"
					+ "\nExamples:"
					+ "\n consume Apple"
					+ "\n eat Burger"
					+ "\n drink Water Bottle"
					+ "\n cs Coffee"
					;
			
		case "use":
		case "u":
			return ""
					+ "\nUSE COMMAND:"
					+ "\n------------"
					+ "\nUsage: use/u <item> on <object>"
					+ "\n"
					+ "\n This command is for interacting with"
					+ "\n objects using items. This is useful for"
					+ "\n unlocking/locking doors with keys."
					+ "\n You must give the command two arguments,"
					+ "\n seperated by an 'or' inbetween."
					+ "\n"
					+ "\nExamples:"
					+ "\n use Old Letter on Teacher"
					+ "\n u Key on Large Door"
					;
			
		case "interact-with":
		case "in":
			return ""
					+ "\nINTERACT COMMAND:"
					+ "\n-----------------"
					+ "\nUsage: interact-with/in <object>"
					+ "\n"
					+ "\n This command lets the player interact"
					+ "\n with objects in their room. This means"
					+ "\n getting more in-depth explanations of"
					+ "\n Things in their surroundings and being"
					+ "\n able to talk to NPCs nearby."
					+ "\n"
					+ "\nExamples:"
					+ "\n interact-with Large Door"
					+ "\n in Teacher"
					;
		
		case "inventory":
		case "items":
		case "iv":
			return ""
					+ "\nINVENTORY COMMAND:"
					+ "\n------------------"
					+ "\nUsage: inventory/items/iv"
					+ "\n"
					+ "\n This command list all items"
					+ "\n currently in the player's inventory."
					+ "\n No arguments are required for this."
					+ "\n"
					+ "\nExamples:"
					+ "\n inventory"
					+ "\n items"
					+ "\n iv"
					;
			
		case "end-turn":
		case "wait":
		case "wt":
			return ""
					+ "\nWAIT COMMAND:"
					+ "\n-------------"
					+ "\nUsage: end-turn/wait/wt"
					+ "\n"
					+ "\n This command simply ends the"
					+ "\n current player's turn and lets"
					+ "\n the next player begin."
					+ "\n This requires no arguments."
					+ "\n"
					+ "\nExamples:"
					+ "\n end-turn"
					+ "\n wait"
					+ "\n wt"
					;
			
		default:
			return "\nI don't recognise that command.";
		}
	}
	
	
	// Prints out opening info
	public static String getOpening() {
		return ""
				+ "\n   ESCAPE FROM TBZ"
				+ "\n  -----------------"
				+ "\n"
				+ "\n   by   Amin Haidar"
				+ "\n    & Samuel Pearce"
				+ "\n"
				+ "\n Version " + VERSION
				+ "\n"
				;
	}

}
