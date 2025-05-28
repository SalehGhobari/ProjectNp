package com.projectnp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Challenger extends Thread {

    public Socket challengerSocket;
    public String nickname;
    public int attempts;
    public String secret; // a public secret isn't really a secret but okay
    public boolean isChallengerTurn = false;
    public String genre;
    public Scanner challengerInput = new Scanner(System.in);

    public Challenger(Socket challengerSocket, String nickname) {
        this.challengerSocket = challengerSocket;
        this.nickname = nickname;
    }
    
    public void initChallenger() {
        System.out.println("Choose a game genre :\n"
                                +"1.Movies\n2.Actors/Actresses\n3.Famous Athletes/Sport Figures\n4.Scientists/Inventors\n5.Historical Figures\n"
                                +"6.Famous Books/Novels\n7.TV Series/Shows\n8.Countries/Cities");
        int genreIndex = challengerInput.nextInt();
        String[] genres = {"Movies", "Actors/Actresses", "Famous Athletes/Sport Figures", "Scientists/Inventors", "Historical Figures",
                    "Famous Books/Novels", "TV Series/Shows", "Countries/Cities"};
        genre = genres[genreIndex - 1];
        System.out.println("Enter A secret for the players to guess : ");
        secret = challengerInput.nextLine();
        System.out.println("Enter Number of guesses for the players (-1 for unlimited guesses)");
        attempts = challengerInput.nextInt();   
    }
    
    @Override
    public void run() {
        try {
            initChallenger();
            BufferedReader challengerReader = new BufferedReader(new InputStreamReader(challengerSocket.getInputStream())); 
            BufferedWriter challengerWriter = new BufferedWriter(new OutputStreamWriter(challengerSocket.getOutputStream()));  
            while (true) { 
                if (isChallengerTurn) {
                    System.out.println("Enter a number :\n 1.Yes\n2.No\n3.I don't know");
                    int index = challengerInput.nextInt();
                    String[] answers = {"Yes", "No", "I don't know"};
                    String answer = answers[index - 1];
                    challengerWriter.write(answer + "\n");
                    challengerWriter.flush();
                    isChallengerTurn = false;
                }
                else {
                    while (!isChallengerTurn) { 
                        System.out.println("Spectating...");
                        System.out.println(challengerReader.readLine());
                    }
                }
            }
        } 
        catch (Exception e) {
            System.out.println("An error occured in the Challenger class " + e);
        }
    }

}
