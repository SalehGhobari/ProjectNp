package com.projectnp;


public class gameRoom {
    public Player player1 = null;
    public Player player2 = null;
    public Challenger challenger;
    public static int roomID = 0;

    public gameRoom(Challenger challenger) {
        this.challenger = challenger;
        roomID++;
    }

    public void addPlayer(Player player) {
        if (player1 == null)
        {
            player1 = player;
        }
        
    }

    
    
}
