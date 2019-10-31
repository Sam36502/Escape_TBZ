package ch.pearcenet.escapetbz;

import ch.pearcenet.escapetbz.exceptions.RoomFullException;

public class Room {
	
	public enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
	
	private String name;
	private String description;
	private Room[] next = new Room[4];
	private Item[] items = new Item[Console.MAX_ITEMS_PER_ROOM];
	private NPC[] npcs = new NPC[Console.MAX_INTERACTABLE_PER_ROOM];
	private Door[] doors = new Door[4];
	
	/// CONSTRUCTORS ///
	
	public Room(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	/// GETTERS & SETTERS ///
	
	@Override
	public String toString() {
		return name;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}

	public Door[] getDoors() {
		return doors;
	}

	public void setDoor(Direction dir, Door newDoor) {
		this.doors[dir.ordinal()] = newDoor;
	}
	
	public NPC[] getNPCs() {
		return npcs;
	}

	public void setNPCs(NPC[] npcs) {
		this.npcs = npcs;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	/// PUBLIC METHODS ///
	
	// Gets the room connected in any given direction
	public Room getNext(Direction dir) {
		return next[dir.ordinal()];
	}
	
	// Gets the room connected in any given direction
	public void setNext(Direction dir, Room nextRm) {
		next[dir.ordinal()] = nextRm;
	}
	
	// Returns whether the player can go in a given direction
	public boolean canGo(Direction dir) {
		
		// If there's no connection in the direction
		if (getNext(dir) == null) {
			return false;
		}
		
		// If there's a locked door in the way
		if (doors[dir.ordinal()] != null &&
			doors[dir.ordinal()].isLocked()) {
			return false;
		}
		
		return true;
	}
	
	// Places an item in this room
	public void addItem(Item item) {
		for (int i=0; i<Console.MAX_ITEMS_PER_ROOM; i++) {
			if (items[i] == null) {
				items[i] = item;
				return;
			}
		}
		throw new RoomFullException("Tried to add item to room that was full.");
	}
	
	// Returns an item matching the name given in this room
	public Item findItem(String name) {
		
		for (Item curr: items) {
			if (curr != null && name.toLowerCase().equals(curr.getName().toLowerCase())) {
				return curr;
			}
		}
		
		return null;
	}
	
	// Removes one item from this room
	public void removeItem(Item item) {
		
		for (int i=0; i<Console.MAX_ITEMS_PER_ROOM; i++) {
			if (items[i] != null && item.getName().equals(items[i].getName())) {
				items[i] = null;
				return;
			}
		}
		
	}
	
	// Returns an Interactable object found in this room by name
	public Interactable findInteractable(String name) {
		
		for (Interactable curr: npcs) {
			if (curr != null && name.toLowerCase().equals(curr.getName().toLowerCase())) {
				return curr;
			}
		}
		
		for (Door curr: doors) {
			if (curr != null && name.toLowerCase().equals(curr.getName().toLowerCase())) {
				return curr;
			}
		}
		
		return null;
		
	}
	
	// Returns a direction enum that matches the string given
	public static Direction parseDirection(String dir) {
		switch(dir.toLowerCase()) {
		
		case "north":
		case "n":
			return Direction.NORTH;
		
		case "east":
		case "e":
			return Direction.EAST;
		
		case "south":
		case "s":
			return Direction.SOUTH;
		
		case "west":
		case "w":
			return Direction.WEST;
			
		}
		
		//UNREACHABLE STATE
		return null;
	}
	
	// Returns the opposite direction of the one chosen
	public static Direction getOpposite(Direction dir) {
		switch(dir) {
		
		case NORTH:
			return Direction.SOUTH;
		
		case SOUTH:
			return Direction.NORTH;
		
		case WEST:
			return Direction.EAST;
		
		case EAST:
			return Direction.WEST;
		
		}
		
		//UNREACHABLE STATE
		return null;
	}
	
}
