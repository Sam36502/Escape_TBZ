package ch.pearcenet.escapetbz;

public class Testing {

	public static void main(String[] args) {
		Level x = null;
		System.out.println("Testing Level Constructors...");
		
		System.out.print("\n TC 1:\n   OUT: ");
		try {
			x = new Level(0);
			System.out.println("-");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		System.out.print("\n TC 2:\n   OUT: ");
		try {
			x = new Level(26);
			System.out.println("-");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		System.out.print("\n TC 3:\n   OUT: ");
		try {
			x = new Level(1);
			System.out.println("-");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		System.out.println("\nTesting Level Methods...");
		
		System.out.print("\n TC 4:\n   OUT: ");
		try {
			x.findRoom("nonexistent");
			System.out.println("-");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		System.out.print("\n TC 5:\n   OUT: ");
		try {
			//x.loadMap("nonexistent");
			System.out.println("-");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		System.out.print("\n TC 6:\n   OUT: ");
		try {
			System.out.println(x.getNumber());
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		System.out.print("\n TC 7:\n   OUT: ");
		try {
			System.out.println(x.getRooms());
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}
		
		
	}

}
