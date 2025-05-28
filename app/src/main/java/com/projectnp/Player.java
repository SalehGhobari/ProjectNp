package com.projectnp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
public class Player extends Thread{

    public Socket playerSocket;
    public String nickname;
    public int attemptsLeft;
    public boolean isPlayerTurn = false;
    public boolean guessing = false;


    public Player(Socket playerSocket, String nickname) {
        this.playerSocket = playerSocket;
        this.nickname = nickname;
    }

    @Override
    public void run() {
        try {
            BufferedReader playerReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            BufferedWriter playerWriter = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            Scanner playerInput = new Scanner(System.in);
            while (true) {  // wait for turn
                if (isPlayerTurn) // if players turn choose to either ask a question or just answer
                {
                    if (!guessing) {
                        // send question to server for challenger
                        System.out.println("Ask a question : \n");
                        String question = playerInput.nextLine();
                        playerWriter.write(question + "\n");
                        playerWriter.flush();
                    }
                    else
                    {
                        System.out.println("Do you want to guess : [Y/N] ");
                        String wantsToGuess = playerInput.nextLine();
                        String guess = "null";
                        if (wantsToGuess.equals("Y") || wantsToGuess.equals("y")) {
                            guess = playerInput.nextLine();
                        }
                        playerWriter.write(guess + "\n");
                        playerWriter.flush();
                    } 
                    isPlayerTurn = false; 
                }
                else { // read other players actions
                    System.out.println("Spectating...");
                    while (!isPlayerTurn)
                    {
                        System.out.println(playerReader.readLine());
                    }
                }
            }
        } 
        catch (Exception e) {
            System.out.println("An error in the player class has occured. " + e);
        }

    }

    
    
}
