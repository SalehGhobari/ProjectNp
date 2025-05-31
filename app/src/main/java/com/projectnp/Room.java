package com.projectnp;

import java.util.concurrent.atomic.AtomicInteger;

public class Room {
    static final AtomicInteger roomCounter = new AtomicInteger(0);
    ClientHandler challenger;
    ClientHandler player1 = null;
    ClientHandler player2 = null;
    int roomID;
    String genre, secret;
    int attempts;
    RoomManager roomManager = RoomManager.getInstance();
    boolean running = true;



    public Room (ClientHandler challenger) {
        this.challenger = challenger;
        this.roomID = roomCounter.incrementAndGet(); // thread-safe unique ID
        roomManager.addRoom(this, this.roomID);
    }

    public String addPlayer(ClientHandler player){
        if(player1 == null) {
            this.player1 = player;
            this.player1.attempts = attempts;
            return player1.name + " has joined the room";
        }
        else {
            this.player2 = player;
            this.player2.attempts = attempts;
            return player2.name + " has joined the room";
        }
    }


    public void setInfo(String genre, String secret, int attempts) {
        this.genre = genre;
        this.secret = secret;
        this.attempts = attempts;

    }

    public synchronized boolean full(){
        return player1 != null && player2 != null;
    }

    public String [] getDetails(){
        String unlimitedAttempts = attempts < 0? "unlimited" : attempts + "";
        String [] details = {"ID: " + roomID,"challenger name: " + challenger.name,
                "genre: " + genre,"attempts: " + unlimitedAttempts,
                "number of players: " + this.getNumberOfPlayers() + "/2" ,"",""};
        if(player1 != null){
            details[5] = "player 1 is: " + player1.name ;
        }
        if(player2 != null){
            details[6] = "player 2 is: " + player2.name ;
        }
        return details;
    }

    public synchronized int getNumberOfPlayers(){
        if(player1 != null && player2 != null){
            return 2;
        }
        else if(player1 != null){
            return 1;
        }
        else return 0;
    }

    public synchronized void broadcast(String msg, ClientHandler sender) {

        if (sender == null) {
            System.out.println("brdcasting: " + msg);
            challenger.send(msg);
            player1.send(msg);
            player2.send(msg);
        } else if (sender.equals(challenger)){
            player1.send(msg);
            player2.send(msg);
            System.out.println("brdcasting from challenger: " + msg);
        }
        else if (sender.equals(player1)){
            challenger.send(msg);
            player2.send(msg);
            System.out.println("brdcasting from p1: " + msg);
        }
        else {
            challenger.send(msg);
            player1.send(msg);
            System.out.println("brdcasting from p2: " + msg);
        }
    }

    public String compareGuess(String guess, ClientHandler sender) {
        if (guess.equals(secret)) {
            return "Correct";
        }
        else {
            if (sender.equals(player1)){
                player1.attempts--;
            }
            else {
                player2.attempts--;
            }
            return "Wrong";
        }
    }

    public ClientHandler checkLoss() {
        if (player1.attempts == 0) {
            return player1;
        }
        else if (player2.attempts == 0) {
            return player2;
        }
        else {
            return challenger ;
        }
    }

    public void endgame(){
        roomManager.remove(this.roomID);

        running = false;
//        try {
//            challenger.socket.close();
//            player1.socket.close();
//            player2.socket.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}
