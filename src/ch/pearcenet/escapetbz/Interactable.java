package ch.pearcenet.escapetbz;

public interface Interactable {
	
	public String getName();
	public String useWith(Item item);
	public boolean canUseWith(Item item);
	public String interact();

}
