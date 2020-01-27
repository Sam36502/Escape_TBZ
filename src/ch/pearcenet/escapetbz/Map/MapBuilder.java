package ch.pearcenet.escapetbz.Map;

import ch.pearcenet.escapetbz.Room;


public class MapBuilder {
    private Room[] rooms;
    private Room startingRoom;
    private Room endingRoom;

    public MapBuilder(Room[] rooms, Room startingRoom, Room endingRoom) {
        this.rooms = rooms;
        this.startingRoom = startingRoom;
        this.endingRoom = endingRoom;
    }

    // Build main map
    public void buildMap(){
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;

        for (Room.Direction direction : Room.Direction.values()){
            int path = getLongestPath(startingRoom, direction, 0);
        }

    }

    // Pathfinding algorithms give me aneurysms
    public int getLongestPath(Room room, Room.Direction direction, int counter){
        counter++;
        Room nextRoom = room.getNext(direction);
        if (nextRoom != null){
            getLongestPath(nextRoom, direction, counter);
        }
        return counter;
    }

    public void printMap(){

    }


    public Room getEndingRoom() {
        return endingRoom;
    }

    public void setEndingRoom(Room endingRoom) {
        this.endingRoom = endingRoom;
    }

    public Room getStartingRoom() {
        return startingRoom;
    }

    public void setStartingRoom(Room startingRoom) {
        this.startingRoom = startingRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }
}
