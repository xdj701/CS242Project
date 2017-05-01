package Game;

import Model.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static Model.utils.GameUtils.*;

/**
 * Created by boyinzhang on 4/9/17.
 */



public class Game implements Serializable{
    public Player[] players;
    public Player currentPlayer;
    public Board gameBoard;

    public Game(){
        this.gameBoard = new Board(NUM_PLAYER);
        this.gameBoard.initialBoard();
        this.players = new Player[NUM_PLAYER];

        for(int i = 0; i < NUM_PLAYER; i++){
            players[i] = new Player(i+1 , this.gameBoard, Integer.toString(i+1));
        }
        currentPlayer = players[0];
    }

    public Game(ArrayList<String> names){
        this.gameBoard = new Board(NUM_PLAYER);
        this.gameBoard.initialBoard();
        this.players = new Player[NUM_PLAYER];

        for(int i = 0; i < NUM_PLAYER; i++){
            players[i] = new Player(i+1 , this.gameBoard, names.get(i));
        }
        currentPlayer = players[0];
    }

    public Board getGameBoard(){
        return this.gameBoard;
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public Player[] getPlayers(){return players;}

    /**
     * Used to check if the someone won.
     * @return
     */


    public final int checkEndofGame(){
        int numberOfWining = 0;
        for(Player player: this.players){
            if(player.hasWon()){
                numberOfWining += 1;
            }
        }
        return numberOfWining;
    }

    /**
     * Turn the next player
     */

    public void turnToNextPlayer(){
        int currentId = currentPlayer.getPlayerId();
        if(currentId == NUM_PLAYER){
            currentId = 0;
        }
        currentPlayer = this.players[currentId];
    }


}
