package com.example.backend.controllers;

import com.example.backend.models.Player;
import com.example.backend.storage.Storage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
@CrossOrigin
public class PlayerController {

    @PostMapping("/create")
    private ResponseEntity<Player> createPlayer(@RequestBody Player player){
        if (Storage.players.contains(player)){
            return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
        }

        Storage.players.add(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/clear-all")
    private void clearPlayers(){
        Storage.players.clear();
    }


}
