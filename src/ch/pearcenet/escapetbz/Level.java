package ch.pearcenet.escapetbz;

import ch.pearcenet.escapetbz.exceptions.LevelNumberException;
import ch.pearcenet.escapetbz.exceptions.RoomNameException;

public class Level {
	
	private int number;
	private Room[] rooms;
	private FileHandler file;
	
	/// CONSTRUCTORS ///
	
	public Level(int number) {
		super();
		
		if (number < 1 || number > Console.MAX_FLOORS) {
			throw new LevelNumberException("'" + number + "' is not a valid floor number.");
		}
		
		this.number = number;
		this.rooms = new Room[Console.MAX_ROOMS_PER_FLOOR];
	}
	
	/// GETTERS ///
	
	public int getNumber() {
		return number;
	}
	
	public Room[] getRooms() {
		return rooms;
	}
	
	/// PRIVATE METHODS ///
	
	// Loads all necessary door information from the filehandler
	private Door loadDoor(String direction, String name) {
		if (file.hasProp(name + "door." + direction)) {
			String doorName = file.getProp(name + "door." + direction);
			String keyName = file.getProp(name + "door." + direction + ".key");
			
			boolean locked = true;
			if (file.hasProp(name + "door." + direction + ".locked"))
				locked = "true".equals(file.getProp(name + "door." + direction + ".locked"));
			
			if (file.hasProp(name + "door." + direction + ".succmsg") &&
				file.hasProp(name + "door." + direction + ".failmsg")) {
				
				String succmsg = file.getProp(name + "door." + direction + ".succmsg");
				String failmsg = file.getProp(name + "door." + direction + ".failmsg");
				
				return new Door(doorName, new Item(keyName), succmsg, failmsg, locked);
			} else {
				return new Door(doorName, new Item(keyName), locked);
			}
		} else {
			return null;
		}
	}
	
	/// PUBLIC METHODS ///
	
	// Finds a room in this level that matches a given name
	public Room findRoom(String name) {
		for (Room curr: rooms) {
			if (curr != null && name.equals(curr.getName())) {
				return curr;
			}
		}
		throw new RoomNameException("Room '" + name + "' couldn't be found.");
	}
	
	// Loads all rooms and their contents from a properties file
	public void loadMap(String filename) {
		//TODO: Set Log Report Level to "WARNING"
		file = new FileHandler(filename, "Level", FileHandler.Lvl.INFO);
		file.loadFile();
		
		file.log(FileHandler.Lvl.INFO, "Loading Rooms...");
		
		// Load all rooms
		for (int roomNum=0; roomNum<Console.MAX_ROOMS_PER_FLOOR; roomNum++) {
			String name = "rm" + roomNum;
			
			if (file.hasProp(name)) {
				
				// Load room info
				String desc = file.getProp(name);
				rooms[roomNum] = new Room(name, desc);
				
				// Load all items in this room
				Item[] items = new Item[Console.MAX_ITEMS_PER_ROOM];
				for (int itemNum=0; itemNum<Console.MAX_ITEMS_PER_ROOM; itemNum++) {
					String id = name + ".i" + itemNum;
					
					if (file.hasProp(id)) {
						
						String itemName = file.getProp(id);
						if (file.hasProp(id + ".hunger") &&
							file.hasProp(id + ".thirst")) {
							
							int hungerRepl = file.getPropInt(id + ".hunger");
							int thirstRepl = file.getPropInt(id + ".thirst");
							
							items[itemNum] = new Item(itemName, hungerRepl, thirstRepl);
						} else {
							items[itemNum] = new Item(itemName);
						}
						
					}
					
				}
				rooms[roomNum].setItems(items);
				
				// Load all the doors into this room
				rooms[roomNum].setDoor(Room.Direction.NORTH, loadDoor("north", name));
				rooms[roomNum].setDoor(Room.Direction.EAST, loadDoor("east", name));
				rooms[roomNum].setDoor(Room.Direction.SOUTH, loadDoor("south", name));
				rooms[roomNum].setDoor(Room.Direction.WEST, loadDoor("west", name));
				
			}
			
		}
		
		file.log(FileHandler.Lvl.INFO, "Done.");
		file.log(FileHandler.Lvl.INFO, "Connecting Rooms...");
		
		// Load room connections
		for (int roomNum=0; roomNum<Console.MAX_ROOMS_PER_FLOOR; roomNum++) {
			String name = "rm" + roomNum;
			
			// Make any northern connections from here
			if (file.hasProp(name + ".north")) {
				Room room = findRoom(file.getProp(name + ".north"));
				if (room != null)
					rooms[roomNum].setNext(Room.Direction.NORTH, room);
			
			// Make any eastern connections from here
			} else if (file.hasProp(name + ".east")) {
				Room room = findRoom(file.getProp(name + ".east"));
				if (room != null)
					rooms[roomNum].setNext(Room.Direction.EAST, room);
			
			// Make any southern connections from here
			} else if (file.hasProp(name + ".south")) {
				Room room = findRoom(file.getProp(name + ".south"));
				if (room != null)
					rooms[roomNum].setNext(Room.Direction.SOUTH, room);
			
			
			// Make any western connections from here
			} else if (file.hasProp(name + ".west")) {
				Room room = findRoom(file.getProp(name + ".west"));
				if (room != null)
					rooms[roomNum].setNext(Room.Direction.WEST, room);
			} else {
				
			}
			
		}
		
		file.log(FileHandler.Lvl.INFO, "Done.");
	}

}
