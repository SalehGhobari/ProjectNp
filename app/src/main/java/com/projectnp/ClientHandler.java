package com.projectnp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    Socket socket;
    String name;
    Room room;
    RoomManager roomManager = RoomManager.getInstance();
    int attempts;
    boolean isTurn = false;
    boolean asking = false;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void send(String msg) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(msg+ "\n");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = reader.readLine(); //ask name and get it
            System.out.println("Client " + name + " connected");
            String mode = reader.readLine(); //ask if want to be a challenger or not and get the result
            if (mode.equals("challenger")){
                Challenger challenger = new Challenger(socket, name);
                challenger.setClientHandler(this);
                this.room = new Room(this);
                challenger.setRoom(room);
                challenger.init(); //continue sequence in the challenger class
                send("room created!");
                while (!room.full()){}
                send("Starting the game: ");
                challenger.challengeLogic();
            }
            else {
                Player player =  new Player(socket,name);
                player.setClientHandler(this);
                //if player mode get all rooms and sent them in order for the player to choose
                ArrayList<Room> rooms = roomManager.getAllRooms();
                if (rooms.isEmpty()){
                    send("No rooms found, try again later.");
                } else {
                    for (Room roomA : rooms) {
                        String[] roomDetails = roomA.getDetails();
                        for (String detail : roomDetails) {
                            send(detail);
                        }
                    }
                    send("CHOOSE");
                    int roomId = Integer.parseInt(reader.readLine()); // desired room ID to join
                    this.room = roomManager.getRoom(roomId);
                    if (room.full()) {
                        send("Room Full"); //if rooms is full it sends that
                    } else if (room == null){
                        send("invalid room ID");
                    } else {
                        room.challenger.send(this.room.addPlayer(this)); //if room is available it joins it and confirms that
                        send("You entered the room");
                        player.setRoom(room);
                        while (!room.full()){}
                        send("Starting the game: ");
                        if (this.equals(room.player1)) {
                            isTurn = true;
                            asking = true;
                            send("TURNS");
                        }
                        player.playerLogic();
                    }
                }

            }
            send("END");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
