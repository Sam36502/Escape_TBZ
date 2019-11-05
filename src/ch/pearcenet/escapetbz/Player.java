package ch.pearcenet.escapetbz;

import java.util.ArrayList;

import ch.pearcenet.escapetbz.exceptions.InventoryFullException;
import ch.pearcenet.escapetbz.exceptions.RoomFullException;
import ch.pearcenet.tui.output.ArrayOutput;

public class Player {
	
	private String name;
	private Room room;
	private ArrayList<Item> inventory;
	private int hunger;
	private int thirst;
	
	/// CONSTRUCTORS ///
	
	public Player(String name) {
		super();
		this.name = name;
		this.inventory = new ArrayList<>(Console.MAX_INVENTORY);
		this.hunger = 100;
		this.thirst = 100;
	}
	
	/// GETTERS & SETTERS ///
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public int getHunger() {
		return hunger;
	}
	
	public void setHunger(int hunger) {
		this.hunger = hunger;
		if (this.hunger > 100) this.hunger = 100;
	}

	public int getThirst() {
		return thirst;
	}
	
	public void setThirst(int thirst) {
		this.thirst = thirst;
		if (this.thirst > 100) this.thirst = 100;
	}
	
	public boolean isAlive() {
		return hunger > 0 && thirst > 0;
	}
	
	/// PUBLIC METHODS ///
	
	// Describes the current room
	public String look() {
		Door[] doors = room.getDoors();
		return ""
				+ "\nRoom:"
				+ "\n " + room.getDescription()
				+ "\n"
				+ "\nDoors:"
				+ "\n North: " + (doors[0]==null ? "Clear":doors[0].getName() + " " + (doors[0].isLocked() ? "[Locked]":"[Unlocked]"))
				+ "\n East: " + (doors[1]==null ? "Clear":doors[1].getName() + " " + (doors[1].isLocked() ? "[Locked]":"[Unlocked]"))
				+ "\n South: " + (doors[2]==null ? "Clear":doors[2].getName() + " " + (doors[2].isLocked() ? "[Locked]":"[Unlocked]"))
				+ "\n West: " + (doors[3]==null ? "Clear":doors[3].getName() + " " + (doors[3].isLocked() ? "[Locked]":"[Unlocked]"))
				+ "\n"
				+ "\nItems:"
				+ "\n " + ArrayOutput.sentenceArray(room.getItems())
				+ "\n"
				+ "\nInteractables:"
				+ "\n " + ArrayOutput.sentenceArray(room.getNPCs())
				+ "\n"
				;
	}
	
	// Updates the hunger/thirst meters after each action
	public void actionTaken() {
		hunger -= Console.HUNGER_PER_ACTION;
		thirst -= Console.THIRST_PER_ACTION;
	}
	
	// Moves the player in a direction if it's possible
	public String move(Room.Direction dir) {
		if (room.canGo(dir)) {
			room = room.getNext(dir);
			actionTaken();
			return room.getDescription();
		} else {
			return "You can't go that way.";
		}
	}
	
	// Drops an item into the current room and removes it from the inventory
	public String drop(Item item) {
		try {
			room.addItem(item);
		} catch (RoomFullException e) {
			return "The room is too full; You can't drop that here";
		}
		
		int itemindex = inventory.indexOf(findItem(item.getName()));
		if (itemindex == -1) {
			return "You don't have that item in your inventory.";
		}
		
		inventory.remove(itemindex);
		return "You dropped the " + item;	

	}
	
	// Picks up an Item from the current room
	public String pickup(String name) {
		Item item = room.findItem(name);
		if (item != null) {
			room.removeItem(item);
			
			try {
				give(item);
			} catch (InventoryFullException e) {
				return "You can't pick that up. Your inventory is full.";
			}
			
			actionTaken();
			return "The " + item + " was added to your inventory";
		} else {
			return "You can't see a '" + name + "' in the room";
		}
	}
	
	// Returns an Item from a player's inventory, if they have it
	public Item findItem(String name) {
		for (Item curr: inventory) {
			if (curr != null && name.toLowerCase().equals(curr.getName().toLowerCase())) {
				return curr;
			}
		}
		return null;
	}
	
	// Adds an item to the player's inventory
	public void give(Item item) {
		if (inventory.size() < Console.MAX_INVENTORY) {
			inventory.add(item);
		} else {
			throw new InventoryFullException("The player's inventory is full. (" + Console.MAX_INVENTORY + " Items)");
		}
	}
	
	// Consumes a consumable item from the player's inventory and replenishes
	// their hunger and thirst meters
	public String consume(Item food) {
		food = findItem(food.getName());
		if (food != null) {
			if (food.isConsumable()) {
				inventory.remove(food);
				hunger += food.getHungerRepl();
				thirst += food.getThirstRepl();
				
				if (hunger > 100) hunger = 100;
				if (thirst > 100) thirst = 100;
				
				return "You had the " + food + " and regained your strength.";
			} else {
				return "You can't consume that.";
			}
		} else {
			return "You don't have that in your inventory.";
		}
	}

}
