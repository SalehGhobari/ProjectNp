package com.projectnp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class RoomManager {

    private final Map<Integer, Room> activeRooms = new ConcurrentHashMap<>();

    private static final RoomManager instance = new RoomManager();

    private RoomManager() { /* ... */ }

    public static RoomManager getInstance() {
        return instance;
    }

    public synchronized void addRoom(Room room, int id) {
        activeRooms.put(id, room);
        System.out.println("Room added: " + id);
    }

    public synchronized void remove(int ID) {
        activeRooms.remove(ID);
    }

    public synchronized ArrayList<Room> getAllRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        Set<Integer> keySet = activeRooms.keySet();
        int[] ids = keySet.stream().mapToInt(Integer::intValue).toArray();
        for (int i :ids ) {
            Room roomA  = activeRooms.get(i);
            if (roomA != null) {
                rooms.add(roomA);
            }
        }
        return rooms;
    }

    public synchronized Room getRoom(int id) {
        return activeRooms.getOrDefault(id, null);

    }

}
