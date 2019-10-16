package ch.pearcenet.escapetbz;

import ch.pearcenet.escapetbz.exceptions.ItemNameException;
import ch.pearcenet.escapetbz.exceptions.ItemReplException;

public class Item {
	
	private String name;
	private boolean consumable;
	private int hungerRepl;
	private int thirstRepl;
	
	/// CONSTRUCTORS ///
	
	public Item(String name) {
		super();
		
		// Check name's validity
		if (!isValidName(name)) {
			throw new ItemNameException("Item name '" + name + "' doesn't contain at least one alphabetic character.");
		}
		
		this.name = name;
		this.consumable = false;
	}
	
	public Item(String name, int hungerRepl, int thirstRepl) {
		super();
		
		// Check name's validity
		if (!isValidName(name)) {
			throw new ItemNameException("Item name '" + name + "' doesn't contain at least one alphabetic character.");
		}
		
		this.name = name;
		
		// Check replenishment numbers' validity
		if (hungerRepl > 100 || hungerRepl < 0 ||
			thirstRepl > 100 || thirstRepl < 0) {
			
			throw new ItemReplException("Item Replenishment can't have a value outside of the range (1-100).");
		}
		
		this.hungerRepl = hungerRepl;
		this.thirstRepl = thirstRepl;
		this.consumable = true;
	}
	
	/// GETTERS ///
	
	public String getName() {
		return name;
	}

	public boolean isConsumable() {
		return consumable;
	}
	
	public int getHungerRepl() {
		return hungerRepl;
	}

	public int getThirstRepl() {
		return thirstRepl;
	}
	
	/// METHODS ///
	
	//Checks whether a name has at least one alphabetic character
	private boolean isValidName(String name) {
		char[] arr = name.toCharArray();
		for (char c: arr) {
			if (Character.isLetter(c)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
