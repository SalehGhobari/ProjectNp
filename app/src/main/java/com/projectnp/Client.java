package com.projectnp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket(InetAddress.getLoopbackAddress(),5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to the guessing game!\n Enter your name: ");
            String name = sc.nextLine();
            writer.write(name + "\n");
            writer.flush();
            System.out.println("Do you want to: \n 1. host a room \n 2. join a room");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1) {
                writer.write("challenger"+ "\n");
                writer.flush();
                System.out.println("""
                        Choose a game genre :
                        1.Movies
                        2.Actors/Actresses
                        3.Famous Athletes/Sport Figures
                        4.Scientists/Inventors
                        5.Historical Figures
                        6.Famous Books/Novels
                        7.TV Series/Shows
                        8.Countries/Cities""");
                int genreIndex = sc.nextInt();
                sc.nextLine();
                String[] genres = {"Movies", "Actors/Actresses", "Famous Athletes/Sport Figures", "Scientists/Inventors", "Historical Figures",
                        "Famous Books/Novels", "TV Series/Shows", "Countries/Cities"};
                writer.write(genres[genreIndex - 1]+ "\n");
                writer.flush();
                System.out.println("Enter a word for the players to guess: ");
                String secret = sc.nextLine();
                writer.write(secret+ "\n");
                writer.flush();
                System.out.println("Do you want to specify a number of attempts? (y/n)");
                String att = sc.nextLine();
                int attempts = -1;
                if (att.equals("y")) {
                    System.out.println("Enter the number of attempts you want: ");
                    attempts = sc.nextInt();
                    sc.nextLine();
                }
                writer.write(Integer.toString(attempts)+ "\n");
                writer.flush();
                System.out.println("Please wait until all the players join");
                String message = reader.readLine();

                while (!message.equals("END")) {
                    if (message.equals("TURNS")) {
                        System.out.println("your turn");
                        System.out.println("Give and answer to the asked question: \n 1. yes \n 2. no \n 3. I don't know\n");
                        int answer = sc.nextInt();
                        sc.nextLine();
                        if (answer == 1) {
                            writer.write("yes"+ "\n");
                            writer.flush();
                        } else if (answer == 2) {
                            writer.write("no"+ "\n");
                            writer.flush();
                        } else {
                            writer.write("I don't know"+ "\n");
                            writer.flush();
                        }
                        message = reader.readLine();
                    } else {
                        System.out.println(message);
                        message = reader.readLine();
                    }
                }
            } else {
                writer.write("player"+ "\n");
                writer.flush();
                System.out.println("List of available rooms (enter room ID):");
                String message = reader.readLine();

                while (!message.equals("CHOOSE")) {
                    if (message.equals("END")) return;
                    System.out.println(message);
                    message = reader.readLine();
                }

                int roomID = sc.nextInt();
                sc.nextLine();
                writer.write(Integer.toString(roomID)+ "\n");
                writer.flush();
                message = reader.readLine();
                while (!message.equals("END")) {
                    if (message.equals("TURNS")) {
                        System.out.println("your turn: ");
                        System.out.println("Ask a question to help you guess the secret word!\n");
                        String question = sc.nextLine();
                        writer.write(question+ "\n");
                        writer.flush();
                        message = reader.readLine();
                    } else if (message.equals("TURNG")) {
                        System.out.println("Do you want to guess now? (y/n)\n");
                        String answer = sc.nextLine();
                        if (answer.equals("y")) {
                            writer.write("yes"+ "\n");
                            writer.flush();
                            System.out.println("enter your guess:");
                            String guess = sc.nextLine();
                            writer.write(guess+ "\n");
                            writer.flush();

                        } else {
                            writer.write("no"+ "\n");
                            writer.flush();
                        }
                        message = reader.readLine();
                    } else {
                        System.out.println(message);
                        message = reader.readLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
