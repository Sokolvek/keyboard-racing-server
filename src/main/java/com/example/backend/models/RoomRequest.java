package com.example.backend.models;

import com.example.backend.enums.PlayersStates;
import com.example.backend.enums.RoomStates;

public record RoomRequest(Room room, RoomStates action) {
}
