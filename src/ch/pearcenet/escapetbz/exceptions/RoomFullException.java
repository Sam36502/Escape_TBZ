package ch.pearcenet.escapetbz.exceptions;

public class RoomFullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RoomFullException (String msg) {
		super(msg);
	}
	
}
