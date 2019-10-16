package ch.pearcenet.escapetbz.exceptions;

public class InventoryFullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InventoryFullException (String msg) {
		super(msg);
	}
	
}
