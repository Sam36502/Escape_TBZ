package ch.pearcenet.escapetbz;

public class Door implements Interactable {
	
	private String name;
	private Item keyItem;
	private boolean locked;
	private String succmsg;
	private String failmsg;
	private String interactmsg;
	
	/// CONSTRUCTORS ///
	
	public Door(String name, Item keyItem) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = true;
		this.succmsg = "The lock clicks and the door swings open";
		this.failmsg = "This won't work on this door.";
		this.interactmsg = "This is a large wooden door.";
	}
	
	public Door(String name, Item keyItem, String succmsg, String failmsg, String interactmsg) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = true;
		this.succmsg = succmsg;
		this.failmsg = failmsg;
		this.interactmsg = interactmsg;
	}
	
	public Door(String name, Item keyItem, boolean locked) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = locked;
		this.succmsg = "The lock clicks and the door swings open";
		this.failmsg = "This won't work on this door.";
		this.interactmsg = "This is a large wooden door.";
	}
	
	public Door(String name, Item keyItem, String succmsg, String failmsg, String interactmsg, boolean locked) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = locked;
		this.succmsg = succmsg;
		this.failmsg = failmsg;
		this.interactmsg = interactmsg;
	}
	
	/// GETTERS & SETTERS ///
	
	public boolean isLocked() {
		return locked;
	}

	public void lock() {
		this.locked = true;
	}
	
	public void toggle() {
		if (this.locked) {
			this.locked = false;
		} else {
			this.locked = true;
		}
	}

	public void unlock() {
		this.locked = false;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/// INTERACTABLE METHODS ///
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String useWith(Item item) {
		
		if (item.getName().equals(keyItem.getName())) {
			this.toggle();
			return succmsg;
		} else {
			return failmsg;
		}

	}

	@Override
	public boolean canUseWith(Item item) {

		if (item.isConsumable()) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public String interact() {
		
		if (this.isLocked()) {
			return interactmsg + " The path is blocked.";
		} else {
			return interactmsg + " The path is open.";
		}
			
	}

}
