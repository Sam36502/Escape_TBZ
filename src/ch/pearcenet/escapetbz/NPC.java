package ch.pearcenet.escapetbz;

import ch.pearcenet.escapetbz.exceptions.InventoryFullException;

public class NPC implements Interactable {
	
	private String name;
	private Item item;
	private String defMsg;
	private String defCmd;
	private String itmMsg;
	private String itmCmd;
	
	/// CONSTRUCTORS ///
	
	public NPC(String name, String defMsg, String defCmd, Item item, String itmMsg, String itmCmd) {
		super();
		this.name = name;
		this.defMsg = defMsg;
		this.defCmd = defCmd;
		
		this.item = item;
		this.itmMsg = itmMsg;
		this.itmCmd = itmCmd;
	}
	
	public NPC(String name, String defMsg, String defCmd) {
		super();
		this.name = name;
		this.defMsg = defMsg;
		this.defCmd = defCmd;
		
		this.item = null;
		this.itmMsg = null;
		this.itmCmd = null;
	}
	
	/// GETTERS & SETTERS ///
	
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
	public String useWith(Player player, Item item) {
		
		if (item != null && this.item.getName().equals(this.item.getName())) {
			try {
				return itmMsg + "\n" + Console.executeCommand(player, itmCmd);
			} catch (InventoryFullException e) {
				return "Your inventory is full";
			}
		} else {
			return "You can't use that on that";
		}

	}

	@Override
	public boolean canUseWith(Item item) {
		return true;
	}

	@Override
	public String interact(Player player) {
		return defMsg + "\n" + Console.executeCommand(player, defCmd);		
	}

}
