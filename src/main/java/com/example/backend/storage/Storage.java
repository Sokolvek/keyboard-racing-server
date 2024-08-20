package com.example.backend.storage;

import com.example.backend.models.Player;
import com.example.backend.models.Room;

import java.util.ArrayList;

public class Storage {
    public static final ArrayList<Room> rooms = new ArrayList<>();
    public static final ArrayList<Player> players = new ArrayList<>();



    public static int roomId = 1;


    public static Room getRoomById(String roomId){
        for (Room room : rooms) {
            if (room.id().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
}
