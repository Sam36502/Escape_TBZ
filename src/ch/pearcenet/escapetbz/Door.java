package ch.pearcenet.escapetbz;

public class Door implements Interactable {
	
	private String name;
	private Item keyItem;
	private boolean locked;
	private String succmsg;
	private String failmsg;
	
	/// CONSTRUCTORS ///
	
	public Door(String name, Item keyItem) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = true;
		this.succmsg = "The lock clicks and the door swings open";
		this.failmsg = "This won't work on this door.";
	}
	
	public Door(String name, Item keyItem, String succmsg, String failmsg) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = true;
		this.succmsg = succmsg;
		this.failmsg = failmsg;
	}
	
	public Door(String name, Item keyItem, boolean locked) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = locked;
		this.succmsg = "The lock clicks and the door swings open";
		this.failmsg = "This won't work on this door.";
	}
	
	public Door(String name, Item keyItem, String succmsg, String failmsg, boolean locked) {
		super();
		this.name = name;
		this.keyItem = keyItem;
		this.locked = locked;
		this.succmsg = succmsg;
		this.failmsg = failmsg;
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
			return "This is a very large door blocking your path.";
		} else {
			return "This is a very large door that is unlocked and open.";
		}
			
	}

}
