package com.projectnp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Player {

    Socket socket;
    String name;
    Room room;


    ClientHandler clientHandler;

    public Player(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void playerLogic(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                if(!room.running){
                    break;
                } else if (clientHandler.isTurn) {
                    synchronized (room) {

                        System.out.println("player " + clientHandler.name + "'s turn");
                        room.broadcast("player " + clientHandler.name + "'s turn", clientHandler);
                        if (clientHandler.asking) {
                            String question = reader.readLine();
                            room.broadcast("player " + clientHandler.name + " asked: ", clientHandler);
                            room.broadcast(question, clientHandler);
                            clientHandler.isTurn = false;
                            room.challenger.isTurn = true;
                            room.challenger.send("TURNS");
                        } else {

                            String wantsToGuess = reader.readLine();
                            if (wantsToGuess.equals("yes")) {
                                room.broadcast(this.name + " wants to guess", clientHandler);
                                String guess = reader.readLine();
                                room.broadcast(guess, clientHandler);
                                String guessResult = room.compareGuess(guess, clientHandler);
                                room.broadcast(guessResult, null);
                                if (room.checkLoss() != null && room.checkLoss().equals(clientHandler)) {
                                    room.broadcast("Player " + this.name + " has lost!", null);
                                    Thread.sleep(2000);
                                    room.endgame();
                                    break;
                                } else if (guessResult.equals("Correct")) {
                                    room.broadcast("Player " + this.name + " has won!", null);
                                    room.endgame();
                                    Thread.sleep(2000);
                                    break;
                                } else {
                                    if (clientHandler.attempts >= 0) {
                                        room.broadcast("Player " + this.name + " has " + clientHandler.attempts + " attempts left", null);
                                    }

                                }
                            } else {
                                room.broadcast(this.name + " doesn't want to guess", null);
                            }
                            clientHandler.isTurn = false;
                            room.challenger.isTurn = true;


                        }
                    }

                } else Thread.sleep(500);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
