package com.example.backend.models;

import java.util.ArrayList;
import java.util.HashMap;

public record Room(String id, ArrayList<Player> players, int limit, boolean gameStarted) {
    public void add(Player player){
        players.add(player);
    }
}
