package ch.pearcenet.escapetbz;

public interface Interactable {
	
	public String getName();
	public String useWith(Player player, Item item);
	public boolean canUseWith(Item item);
	public String interact(Player player);

}
