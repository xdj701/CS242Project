package Controller;

import Game.Game;
import Model.*;
import Model.utils.GemInfo;
import View.BoardUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import static Model.utils.GameUtils.*;
import static View.utils.ViewUtils.*;

/**
 * Created by boyinzhang on 4/17/17.
 */
public class Controller{
    public Game game;
    public BoardUI boardUI;
    private Card selectedCard;
    private GemInfo currentGemInfo;


    private ObjectOutputStream out;
    private int id;
    private String name;

    public Controller(){
        this.game = new Game();
        this.boardUI = new BoardUI(game,"anonymous");
        this.currentGemInfo = new GemInfo(0);
        this.id = 0;
        addMenuItemListener(false);
        addGemsListener();
        addCardListeners();
        addFunctionalListeners(false);
    }

    public Controller(Game game, ObjectOutputStream out, int id, String name){
        this.id = id;
        this.name = name;
        this.game = game;
        this.out = out;
        this.boardUI = new BoardUI(game,name);
        this.currentGemInfo = new GemInfo(0);
        addMenuItemListener(true);
        addGemsListener();
        addCardListeners();
        addFunctionalListeners(true);
    }

    public Controller(Game game, ObjectOutputStream out, int id, boolean isAi){
        this.id = id;
        this.name = "ai";
        this.game = game;
        this.out = out;
        this.currentGemInfo = new GemInfo(0);

    }


    /**
     * Helper function to help initialize the gui
     */
    private void addMenuItemListener(boolean serverMode){
        addNewGameListener(serverMode);
        addExitListener(serverMode);
    }

    /**
     * for JMenubar New
     */
    private void addNewGameListener(boolean serverMode) {
        boardUI.addNewGameListener(new ActionListener(){
            public void actionPerformed(ActionEvent event) {

                if(serverMode)
                    sendVoteResult("RESTART");
                else
                    newGame();
            }
        });
    }

    private void newGame() {
        game = new Game();
        boardUI.updateByGame(game);
        currentGemInfo = new GemInfo(0);
        selectedCard = null;
    }

    /**
     * for JMenubar Exit
     */
    private void addExitListener(boolean serverMode) {
        boardUI.addExitListener(new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                if(serverMode)
                    requestServer("EXIT","EXIT");
                System.exit(0);
            }
        });
    }


    /**
     * Add listeners of gems.
     */
    private void addGemsListener(){
        this.boardUI.getGems()[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //double click will not cancel the effect
                currentGemInfo.updateInfo(1,0,0,0,0);
                if(currentGemInfo.diamond >= 2)
                    boardUI.getGems()[4].setSelected(true);
            }
        });

        this.boardUI.getGems()[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentGemInfo.updateInfo(0,1,0,0,0);
                if(currentGemInfo.emerald >= 2)
                    boardUI.getGems()[3].setSelected(true);
            }
        });

        this.boardUI.getGems()[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentGemInfo.updateInfo(0,0,1,0,0);
                if(currentGemInfo.onyx >= 2)
                    boardUI.getGems()[2].setSelected(true);
            }
        });

        this.boardUI.getGems()[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentGemInfo.updateInfo(0,0,0,1,0);
                if(currentGemInfo.ruby >= 2)
                    boardUI.getGems()[1].setSelected(true);
            }
        });

        this.boardUI.getGems()[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentGemInfo.updateInfo(0,0,0,0,1);
                if(currentGemInfo.sapphire >= 2)
                    boardUI.getGems()[0].setSelected(true);
            }
        });
    }

    /**
     * Add listeners to the cards on board and reserved cards
     */
    private void addCardListeners(){
        for(int i = 0; i < NUM_CARD_RANK; i++){
            for(int j = 0; j < NUM_CARD_PER_RANK; j ++){
                int finalI = i;
                int finalJ = j;
                this.boardUI.getCards()[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedCard = game.getGameBoard().getCards()[finalI][finalJ];
                        selectedCard.setPosition(finalI,finalJ);
                    }
                });
            }
        }
        for(int i = 0; i < NUM_PLAYER; i++){
            for(int j = 0; j < MAX_RESERVED_CARDS; j++){
                int finalI = i;
                int finalJ = j;
                this.boardUI.getPlayers()[i].getReservedCards()[j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedCard = game.getPlayers()[finalI].getReserves().get(finalJ);
                        selectedCard.setPosition(finalI,finalJ);
                    }
                });
            }
        }
    }

    /**
     * Indicator whether it is used for server mode.
     * @param serverMode true if it is the server mode
     */
    private void addFunctionalListeners(boolean serverMode){
        addResetListener(serverMode);
        addCollectListener(serverMode);
        addBuyListener(serverMode);
        addReserveListener(serverMode);
    }


    /**
     * Reset all buttons
     */
    private void resetButtons(){

        //reset cards
        for(int i = 0; i < NUM_CARD_RANK; i++){
            for(int j = 0; j < NUM_CARD_PER_RANK; j ++) {
                boardUI.getCards()[i][j].setSelected(false);
            }
        }
        selectedCard = null;

        //reset gems
        for(int i = 0; i < 5; i++)
            boardUI.getGems()[i].setSelected(false);

        //clean up gem infos
        currentGemInfo.reset();
    }

    /**
     * Add listener to the reset button
     */

    private void addResetListener(boolean serverMode){
        this.boardUI.getReset().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(serverMode) {
                    if (game.getCurrentPlayer().getPlayerId() != id + 1) {
                        JOptionPane.showMessageDialog(null, "Wait for your opponent's move",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                resetButtons();
            }
        });
    }

    /**
     * Add listener to the collect button
     */

    private void addCollectListener(boolean serverMode){
        this.boardUI.getCollect().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(serverMode) {
                    if (game.getCurrentPlayer().getPlayerId() != id + 1) {
                        JOptionPane.showMessageDialog(null, "Wait for your opponent's move",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                    boolean status = game.getCurrentPlayer().collectGems(currentGemInfo);
                    if (!status) {
                        JOptionPane.showMessageDialog(null, "Invalid Collection! Please make another try!",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        resetButtons();
                    } else {
                        String command = String.valueOf(currentGemInfo.diamond) + ' ' + String.valueOf(currentGemInfo.emerald) + ' '
                                + String.valueOf(currentGemInfo.onyx) + ' ' + String.valueOf(currentGemInfo.ruby) + ' '
                                + String.valueOf(currentGemInfo.sapphire);
                        resetButtons();
                        if(serverMode)
                            requestServer("COLLECT", command);
                        else{
                            if (checkEnd(false)) return;
                            game.turnToNextPlayer();
                            boardUI.updateByGame(game);
                        }
                    }



            }
        });
    }

    /**
     * Add listener to the buy button
     */
    private void addBuyListener(boolean serverMode){
        this.boardUI.getBuy().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(serverMode) {
                    if (game.getCurrentPlayer().getPlayerId() != id + 1) {
                        JOptionPane.showMessageDialog(null, "Wait for your opponent's move",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if(selectedCard == null){
                    JOptionPane.showMessageDialog(null, "Must select one card!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean status = game.getCurrentPlayer().buyCard(selectedCard,selectedCard.isReserved());
                if(!status){
                    JOptionPane.showMessageDialog(null, "Cannot buy that card! Please make another try!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    selectedCard = null;
                }
                else {
                        String command;
                        if (selectedCard.isReserved()) {
                            command = String.valueOf(selectedCard.getPosition()[0]) + ' ' + String.valueOf(selectedCard.getPosition()[1])
                                    + " 1";
                        } else {
                            command = String.valueOf(selectedCard.getPosition()[0]) + ' ' + String.valueOf(selectedCard.getPosition()[1])
                                    + " 0";
                        }

                        Card newCard = game.getGameBoard().getNewCard(selectedCard.getPosition()[0]);
                        game.getGameBoard().setCardOnBoard(newCard, selectedCard.getPosition());
                        resetButtons();
                        game.getCurrentPlayer().recruitAvailableNobles();
                        if(serverMode) {
                            if (checkEnd(true)) {
                                game.turnToNextPlayer();
                                try {
                                    out.writeObject("VICTORY");
                                    out.writeObject(game);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                return;
                            }
                            game.turnToNextPlayer();
                            requestServer("PURCHASE", command);
                        }
                        else{
                            if (checkEnd(false)) return;
                            game.turnToNextPlayer();
                            boardUI.updateByGame(game);
                        }
                }

            }
        });
    }


    private boolean checkEnd(boolean serverMode) {

        if(serverMode) {
            if (game.currentPlayer.hasWon())
                return true;
        }
        //local mode
        else {
            if (game.getCurrentPlayer().getPlayerId() == NUM_PLAYER) {
                int numberOfWining = game.checkEndofGame();
                if (numberOfWining == 1) {
                    for (Player player : game.getPlayers()) {
                        if (player.hasWon()) {
                            int replyNewGame = JOptionPane.showConfirmDialog(null,
                                    "Player " + player.getPlayerId() + " win! Do you want to start a new game", "Yes?", JOptionPane.YES_NO_OPTION);
                            if (replyNewGame == JOptionPane.YES_OPTION) {
                                newGame();
                                return true;
                            }
                        }
                    }
                } else if (numberOfWining > 1) {
                    int replyNewGame = JOptionPane.showConfirmDialog(null,
                            "Tie! Do you want to start a new game", "Yes?", JOptionPane.YES_NO_OPTION);
                    if (replyNewGame == JOptionPane.YES_OPTION) {
                        newGame();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Add listener to the reserve button
     */
    private void addReserveListener(boolean serverMode){
        this.boardUI.getReserve().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Reserve");
                if(serverMode) {
                    if (game.getCurrentPlayer().getPlayerId() != id + 1) {
                        JOptionPane.showMessageDialog(null, "Wait for your opponent's move",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if(selectedCard == null){
                    JOptionPane.showMessageDialog(null, "Must select one card!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean status = game.getCurrentPlayer().reserveCard(selectedCard);
                if(!status){
                    JOptionPane.showMessageDialog(null, "Cannot reserve that card! Please make another try!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    selectedCard = null;
                }
                else{
                    String command = String.valueOf(selectedCard.getPosition()[0]) + ' ' + String.valueOf(selectedCard.getPosition()[1]);

                    Card newCard = game.getGameBoard().getNewCard(selectedCard.getPosition()[0]);
                    game.getGameBoard().setCardOnBoard(newCard, selectedCard.getPosition());

                    game.turnToNextPlayer();
                    resetButtons();
                    //boardUI.window.setEnabled(false);
                    if(serverMode) {
                        requestServer("RESERVE", command);
                        System.out.println("Reserve request made");
                    }
                    else {
                        boardUI.updateByGame(game);
                        if (checkEnd(false)) return;
                    }
                }
            }
        });
    }


    void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();

        for(int i = 0; i < components.length; i++) {
            if(components[i].getClass().getName() == "javax.swing.JPanel") {
                setPanelEnabled((JPanel) components[i], isEnabled);
            }

            components[i].setEnabled(isEnabled);
        }
    }

    /**
     * Send text information and the game to server
     * @param msg text indicator
     */

    public void requestServer(String msg, String command){
        try {
            out.reset();
            out.writeObject(msg);
            out.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send text information to server
     * @param msg text information
     */
    public void sendVoteResult(String msg){
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ask each player if they agree to start a new game, send the result to server.
     */
    public void voteForNewGame(){
        int replyNewGame = JOptionPane.showConfirmDialog(null,
                name+", Do you want to start a new game","Yes?",JOptionPane.YES_NO_OPTION);
        if(replyNewGame==JOptionPane.YES_OPTION) {
            sendVoteResult("AGREE");
        }
        else{
            sendVoteResult("DECLINE");
        }
    }

    public static void main(String[] args) throws IOException {
        new Controller();
    }
}
