package com.projectnp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Challenger  {
    Socket socket;
    String name;
    Room room;

    ClientHandler clientHandler;

    public Challenger(Socket socket, String name) {

        this.socket = socket;
        this.name = name;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void init (){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String genre = reader.readLine();
            String secret = reader.readLine();
            int attempts = Integer.parseInt(reader.readLine());
            room.setInfo(genre, secret, attempts);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void challengeLogic(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int theTurn = 1;
            while (true) {
                if(!room.running){
                    break;
                }else if(clientHandler.isTurn) {
                    synchronized (room) {
                        if (theTurn == 1) {

                            System.out.println("client" + clientHandler.name + "'s turn");
                            room.broadcast("player " + clientHandler.name + "'s turn", clientHandler);
                            if (room.player1.asking) {
                                System.out.println("challenger answering");
                                String answer = reader.readLine();
                                room.broadcast("client answered: ", clientHandler);
                                room.broadcast(answer, clientHandler);
                                room.player1.asking = false;
                                room.player1.isTurn = true;
                                room.player1.send("TURNG");
                            } else {
                                room.player1.asking = true;
                                room.player1.isTurn = false;
                                theTurn = 2;
                                room.player2.asking = true;
                                room.player2.isTurn = true;
                                room.player2.send("TURNS");
                            }
                            clientHandler.isTurn = false;
                        } else {
                            if (room.player2.asking) {
                                String answer = reader.readLine();
                                room.broadcast(answer, clientHandler);
                                room.player2.asking = false;
                                room.player2.isTurn = true;
                                room.player2.send("TURNG");
                            } else {
                                room.player2.asking = true;
                                room.player2.isTurn = false;
                                theTurn = 1;
                                room.player1.asking = true;
                                room.player1.isTurn = true;
                                room.player1.send("TURNS");
                            }
                            clientHandler.isTurn = false;
                        }
                    }
                }  else Thread.sleep(500);
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
