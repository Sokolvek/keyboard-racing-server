package com.example.backend.controllers;

import com.example.backend.enums.RoomStates;
import com.example.backend.models.Player;
import com.example.backend.models.Room;
import com.example.backend.models.RoomRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GreetingController {

    private final HashMap<String, Player> players = new HashMap<>();

    int id = 0;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public HashMap<String, Player> greeting(Player player) throws Exception {
        System.out.println(player.wordIndex);
        if(players.size() > 0 && players.containsKey(player.name)){
            System.out.println("changed");
            players.get(player.name).wordIndex = player.wordIndex;
        }

        if (players.containsKey(player.name)){
            return players;
        }
        this.id += 1;
        players.put(player.name, player);
        return players;
    }

//    @MessageMapping("/rooms/{roomId}")
//    @SendTo("topic/rooms/{roomId}")
//    public void roomControl(RoomRequest room, Player player){
//        if(room.action() == RoomStates.CREATE){
//            Room newRoom = new Room();
//            newRoom.id = 0;
//            newRoom.players.add(player);
//            rooms.add(newRoom);
//        }
//
//    }

}