package com.example.backend.controllers;

import com.example.backend.enums.RoomStates;
import com.example.backend.models.Player;
import com.example.backend.models.Room;
import com.example.backend.models.RoomRequest;
import com.example.backend.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class RoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired private SimpUserRegistry userRegistry;
    public void printConnectedUsers() {

    }


    @PostMapping("/room-create")
    public Room createRoom(@RequestBody Player player){
        Room room = new Room(Integer.toString(Storage.roomId), new ArrayList<Player>(List.of(player)), 4, false);
        Storage.rooms.add(room);
        Storage.roomId += 1;
        messagingTemplate.convertAndSend("/topic/rooms/all-rooms", Storage.rooms);
        return room;
    }

    @PostMapping("/join-room/{id}")
    public Room joinRoom(@RequestBody Player player, @PathVariable String id){

        Room room = Storage.getRoomById(id);

        assert room != null;
        if(room.gameStarted()){
            return null;
        }
        room.add(player);

        log.info("Player {} joined room {}", player, id);
        messagingTemplate.convertAndSend("/topic/room/" + id, room);
        messagingTemplate.convertAndSend("/topic/rooms/all-rooms", Storage.rooms);
        return room;
    }

    @MessageMapping("/room/{id}")
        public Room roomControl(@Payload RoomRequest roomReq) {
        if (roomReq.action() == RoomStates.START){
            var room = roomReq.room();
            Room newRoom = new Room(room.id(), room.players(), room.limit(), true);
            messagingTemplate.convertAndSend("/topic/rooms/" + roomReq.room().id(), newRoom);
            return newRoom;
        }
        System.out.println("room socket " + " " + roomReq.room().id());
        messagingTemplate.convertAndSend("/topic/rooms/" + roomReq.room().id(), roomReq.room());
        return roomReq.room();
    }

    @MessageMapping("/rooms")
    public ArrayList<Room> allRooms(@Payload RoomRequest request){
        System.out.println("called " + request.action());
        messagingTemplate.convertAndSend("/topic/rooms", Storage.rooms);
        return Storage.rooms;
    }


    @GetMapping("/test")
    void test(){
        System.out.println("called");
        userRegistry.getUsers().stream()
                .map(u -> u.getName())
                .forEach(System.out::println);

    }
}
