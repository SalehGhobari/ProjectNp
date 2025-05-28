package com.projectnp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int ServerPortNumber = 5000;
    static ServerSocket GameServer; // create a ServerSocket to accept connections
    public static void main(String[] args) {
        // for this server code textual prompts are handled on the client side for simplicity 
        try {
            GameServer = new ServerSocket(ServerPortNumber);
            
            while (true) { 
               Socket challenger = GameServer.accept();
               BufferedReader challengerRead = new BufferedReader(new InputStreamReader(challenger.getInputStream()));
               BufferedWriter challengerWrite = new BufferedWriter(new OutputStreamWriter(challenger.getOutputStream()));
                
               System.out.println("Accepted Game Connection from " + challenger.getInetAddress());
            //    challengerWrite.write("Welcome to the guessing game \n" +
            //                         "Choose A nickname :");
            //    challengerWrite.flush();
               String challengerNickname = challengerRead.readLine();
            //    challengerWrite.write("Choose A genre to guess from :\n"
            //                          +"1.Movies\n2.Actors/Actresses\n3.Famous Athletes/Sport Figures\n4.Scientists/Inventors\n5.Historical Figures\n"
            //                          +"6.Famous Books/Novels\n7.TV Series/Shows\n8.Countries/Cities");
            //     challengerWrite.flush();
                int genreNum = Integer.parseInt(challengerRead.readLine());
                String[] genres = {"Movies", "Actors/Actresses", "Famous Athletes/Sport Figures", "Scientists/Inventors", "Historical Figures",
                                  "Famous Books/Novels", "TV Series/Shows", "Countries/Cities"};
                // wait for second player ?
                String genre = genres[genreNum - 1];
                // challengerWrite.write("Enter a secret to be guessed : \n");
                String secret = challengerRead.readLine();
            //  prompt for number of attempts if -1 unlimted attempts if 0 invalid reject and if > 0 set numbers of attempts 
            //  handle textual prompts on client side
                int attemptCount = Integer.parseInt(challengerRead.readLine());
                // while(attemptCount == 0)
                // {
                //     attemptCount = Integer.parseInt(challengerRead.readLine());
                // }
                while (attemptCount == 0) {
                  attemptCount = Integer.parseInt(challengerRead.readLine());  
                }
                // somehow handle waiting for other player (threading)
                if (attemptCount < 0) {
                    while (true) { 
                        
                    }
                }
                
            }


        } catch (Exception e) {
            System.out.println("An Error occured");
        }
        
    }
}
