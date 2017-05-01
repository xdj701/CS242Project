package Network;

import Controller.*;
import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Created by boyinzhang on 4/22/17.
 */
public class Client {

    private static int PORT = 8080;
    private Socket socket;


    Scanner keyboardInput = null;

    private ObjectInputStream flowInput = null;
    private ObjectOutputStream flowOutput = null;
    private Controller controller;

    private boolean ai;
    private int clientID;
    private String username;

    public Client(String serverAddress) throws Exception {

        // Setup networking
        socket = new Socket(serverAddress, PORT);
        System.out.println("Connected to " + socket.getRemoteSocketAddress());

        flowInput = new ObjectInputStream(socket.getInputStream());
        flowOutput = new ObjectOutputStream(socket.getOutputStream());

        String msg = (String)flowInput.readObject();
        System.out.println(msg);

        Login loginDlg = new Login(null,flowInput,flowOutput);
        loginDlg.setVisible(true);

        /*
        //get username from keyboard
        keyboardInput = new Scanner(System.in);
        String name = keyboardInput.nextLine();

        //send username back
        flowOutput.writeObject(name);

*/

        //fetch id and game
        clientID = (Integer) flowInput.readObject();
        Game game = (Game) flowInput.readObject();
        String name = game.players[clientID].getName();

        if(name.startsWith("ai")){
            ai = true;
            System.out.println("This client is controlled by AI!");
            controller = new Controller(game, flowOutput, clientID, true);
        }
        else{
            ai = false;
            controller = new Controller(game, flowOutput, clientID, name);
        }


        //notify server the local game is ready
        flowOutput.writeObject(name+" initialized");

        //out.writeObject(game);
    }


    public void play() throws ClassNotFoundException, IOException {
        if(!ai){
            while(true){
                String response;
                try {
                    response = (String) flowInput.readObject();
                    if(response.startsWith("WIN")){
                        System.out.println("You win :)");
                        break;
                    }
                    else if(response.startsWith ("TIE")){
                        ArrayList<Integer> playerList = (ArrayList<Integer>) flowInput.readObject();
                        playerList.remove(new Integer(clientID));
                        System.out.printf("You tie with:");
                        for(int i = 0; i<playerList.size();i++){
                            System.out.printf(" %d",playerList.get(i));
                        }
                        System.out.println(" !");
                        break;
                    }
                    else if(response.startsWith("LOSE")){
                        System.out.println("You lose :(");
                        break;
                    }
                    else if(response.startsWith("MOVE")){

                        //enable all buttons
                    }
                    else if(response.startsWith("UPDATE")){
                        Game updatedGame = (Game) flowInput.readObject();
                        controller.game = updatedGame;
                        controller.boardUI.updateByGame(updatedGame);
                    }
                    else if(response.startsWith("VOTE")){
                        controller.voteForNewGame();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            while(true) {
                String response;
                try {
                    response = (String) flowInput.readObject();
                    if(response.startsWith("UPDATE")){
                        Game updatedGame = (Game) flowInput.readObject();
                        controller.game = updatedGame;
                        if(controller.game.getCurrentPlayer().getPlayerId() == clientID+1){
                            controller.requestServer("COLLECT","1 1 1 0 0");
                        }
                    }
                    else if(response.startsWith("VOTE")){
                        controller.sendVoteResult("AGREE");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        socket.close();
        System.out.println("Game Over");
    }


    //main
    public static void main(String[] args) throws Exception {


        Object[] options = {"Yes, help me find the other players! ", "No, I want to start a local game!"};
        int local = JOptionPane.showOptionDialog(null,
                "Do you want to connect to the internet?",
                "Connection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title

        if(local == 1){

            int localAI = JOptionPane.showOptionDialog(null,
                    "Do you want to play with AI?",
                    "AI",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title
            if(localAI == 1){

            }
            else {
                Controller c = new Controller();
            }
        }
        else {
            String serverAddress = (args.length == 0) ? "localhost" : args[1];
            Client client = new Client(serverAddress);
            client.play();
        }

    }

}