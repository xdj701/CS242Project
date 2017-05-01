package View;

import Model.Noble;
import Model.Player;
import Model.utils.GemInfo;


import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import static Model.utils.GameUtils.*;
import static View.utils.ViewUtils.*;
import Game.Game;
import Model.*;

/**
 * Created by Yu on 4/16/17.
 */
public class BoardUI {

    public JFrame window;
    private JPanel gameArea;
    private JPanel playerArea;

    private JPanel decks[];
    private JPanel nobles[];
    private JButton gold;
    private JToggleButton gems[];
    private JToggleButton cards[][];
    private PlayerPanel players[];
    private JButton collect, reset, buy, reserve;

    private JMenuItem newGame;
    private JMenuItem exit;

    private Hashtable<String, Image> cardImages = new Hashtable<String, Image>();
    private Hashtable<String, Image> gemImages = new Hashtable<String, Image>();
    private Hashtable<String, Image> nobleImages = new Hashtable<String, Image>();

    public BoardUI() {

        loadImagesInMemory();
        window = new JFrame("Splendor");
        window.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        window.setMaximumSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        window.setMinimumSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));

        final JMenuBar menuBar = createMenuBar();
        this.window.setJMenuBar(menuBar);

        setLayout();

        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public BoardUI(Game game, String name) {

        loadImagesInMemory();
        window = new JFrame("Splendor @" + name);
        window.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        window.setMaximumSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        window.setMinimumSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));

        final JMenuBar menuBar = createMenuBar();
        this.window.setJMenuBar(menuBar);

        this.setLayout();
        this.updateByGame(game);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void updateByGame(Game game){
        Noble[] nobles = game.gameBoard.getNobles();
        for (int i=0;i<NUM_PLAYER+1;i++) {
            if(!nobles[i].getIsRecruited()){
                Noble nobleInput = new Noble(nobles[i].getThreshold().diamond, nobles[i].getThreshold().emerald, nobles[i].getThreshold().onyx, nobles[i].getThreshold().ruby, nobles[i].getThreshold().sapphire);
                this.nobles[i].add(new NoblePanel(nobleInput,nobleImages), BorderLayout.CENTER);
            }
            else {
                this.nobles[i].removeAll();
                this.nobles[i].validate();
                this.nobles[i].repaint();
            }
        }

        GemInfo availableGems = game.gameBoard.getAvailableGem();
        for (int i=0;i<5;i++) {
            gems[i].removeAll();
            gems[i].setOpaque(false);
            gems[i].setContentAreaFilled(false);
            gems[i].setBorderPainted(false);
            gems[i].setIcon(plotGemButton(getGemByIndex(5-i), availableGems.getByIndex(5-i), gemImages,GEM_WIDTH,false));
            gems[i].setSelectedIcon(plotGemButton(getGemByIndex(5-i), availableGems.getByIndex(5-i), gemImages,GEM_WIDTH,true));
        }

        Card[][] currentCards = game.gameBoard.getCards();
        for (int i=0;i<NUM_CARD_RANK;i++){
            for (int j=0;j<NUM_CARD_PER_RANK;j++){
                cards[i][j].removeAll();
                cards[i][j].setOpaque(false);
                cards[i][j].setContentAreaFilled(false);
                cards[i][j].setBorderPainted(false);
                cards[i][j].setIcon(plotCardButton(currentCards[i][j],gemImages,cardImages,false));
                cards[i][j].setSelectedIcon(plotCardButton(currentCards[i][j],gemImages,cardImages,true));

            }
        }

        int availableGold = game.gameBoard.getAvailableGolds();
        gold.removeAll();
        gold.setOpaque(false);
        gold.setContentAreaFilled(false);
        gold.setBorderPainted(false);
        gold.setIcon(plotGoldButton(availableGold,gemImages,GEM_WIDTH,false));

        //gold.setRolloverEnabled(true);

        Player[] gamePlayers = game.getPlayers();
        for (int i=0;i<NUM_PLAYER;i++) {
            if (game.getCurrentPlayer().getPlayerId() == gamePlayers[i].getPlayerId()) {
                this.players[i].setStatus(gamePlayers[i].getName(), gamePlayers[i].getScore(), true);
            } else {
                this.players[i].setStatus(gamePlayers[i].getName(), gamePlayers[i].getScore(), false);
            }
            this.players[i].setGems(gamePlayers[i].getGems(), gamePlayers[i].getGolds());
            this.players[i].setCards(gamePlayers[i].getCards());
            this.players[i].setReservedCards(gamePlayers[i].getReserves(),gemImages,cardImages);
        }
        return;
    }
    /**
     * The init function to set up the menu bar
     * @return the menu bar to be created
     */
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        final JMenu fileMenu = new JMenu("File");
        newGame = new JMenuItem("New");
        exit = new JMenuItem("Exit");
        fileMenu.add(newGame);
        fileMenu.add(exit);
        menuBar.add(fileMenu);
        return menuBar;
    }

    /*
    The following methods are used to add listeners to the JMenubar.
     */
    public void addNewGameListener(ActionListener a) {
        newGame.addActionListener(a);
    }

    public void addExitListener(ActionListener a) {
        exit.addActionListener(a);
    }

    public JToggleButton[] getGems(){
        return this.gems;
    }

    public JButton getGold(){
        return this.gold;
    }

    public JToggleButton[][] getCards(){
        return this.cards;
    }

    public JButton getReset(){
        return this.reset;
    }

    public JButton getCollect(){
        return this.collect;
    }

    public JButton getBuy(){
        return this.buy;
    }

    public JButton getReserve(){
        return this.reserve;
    }

    //Store the image to the selected dict and use the truncated filename as key
    public void storeInMap(Hashtable<String,Image> dict, int offset, File f, int w, int h){
        try {
            BufferedImage img = null;
            img = ImageIO.read(f);
            Image dimg = img.getScaledInstance(w,h,Image.SCALE_SMOOTH);
            String img_name = f.getName();
            dict.put(img_name.substring(0,img_name.length()-offset),dimg);
        }catch (final IOException e) {
            // handle errors here
            System.out.println("Images read error");
        }
    }

    //load images in dir to memory
    public void loadImagesInMemory() {
        final File dir = new File("src/View/img");
        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles()) {
                //load images of gems
                if (f.getName().endsWith("Gem.png")) {
                    storeInMap(gemImages, 7, f, GEM_WIDTH, GEM_HEIGHT);
                }
                //load images of nobles
                else if (f.getName().endsWith("Noble.jpg")) {
                    storeInMap(nobleImages, 9, f, NOBLE_WIDTH, NOBLE_HEIGHT);
                }
                //load images of cards
                else {
                    storeInMap(cardImages, 4, f, CARD_WIDTH, CARD_HEIGHT);
                }
            }
        }
    }

    public PlayerPanel[] getPlayers(){
        return this.players;
    }

    private void setLayout() {

        try {
            gameArea = new GamePanel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameArea.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
        gameArea.setLayout(null);
        setGameBoard();
        window.add(gameArea,BorderLayout.WEST);


        playerArea = new JPanel(){
            //load background
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                File f = new File("src/View/img/PlayerBackground.jpg");
                BufferedImage image = null;
                try {
                    image = ImageIO.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image backgroundImage = image.getScaledInstance(PLAYER_WIDTH,WINDOW_HEIGHT,Image.SCALE_SMOOTH);
                g.drawImage(backgroundImage,0,0,null);

            }
        };
        playerArea.setPreferredSize(new Dimension(PLAYER_WIDTH,PLAYER_HEIGHT));
        setPlayerArea();
        window.add(playerArea,BorderLayout.EAST);
    }

    private void setGameBoard() {

        int offset = ratio * 2;
        nobles = new JPanel[NUM_PLAYER+1];
        for (int i=0;i<NUM_PLAYER+1;i++){
            nobles[i] = new JPanel(new BorderLayout());

            nobles[i].setBounds(offset/2+(NOBLE_WIDTH*2) * (i+1) *ratio/100, 50*ratio/100, NOBLE_WIDTH,NOBLE_HEIGHT);
            gameArea.add(nobles[i]);
        }

        gold = new JButton("gold");
        gold.setBounds(ratio/2+75*ratio/100,150*ratio/100, GEM_WIDTH, GEM_HEIGHT);
        gameArea.add(gold);

        gems = new JToggleButton[5];
        for (int i=0;i<5;i++) {
            gems[i] = new JToggleButton("tmp");
            gems[i].setBounds(ratio/2+75*ratio/100, (775-125*i)*ratio/100, GEM_WIDTH, GEM_HEIGHT);
            gameArea.add(gems[i]);
        }

        decks = new JPanel[NUM_CARD_RANK];
        ArrayList<String> selColors = new ArrayList(Arrays.asList("green","red","blue"));
        for(int i = 0 ; i< NUM_CARD_RANK; i++){
            decks[i] = new DeckPanel(selColors.get(NUM_CARD_RANK-1-i));
            decks[i].setBounds(offset+ratio, ratio/3+(200+225*i)*ratio/100, CARD_WIDTH,CARD_HEIGHT);
            gameArea.add(decks[i]);
        }

        cards = new JToggleButton[NUM_CARD_RANK][NUM_CARD_PER_RANK];
        for (int i=0;i<NUM_CARD_RANK;i++){
            for (int j=0;j<NUM_CARD_PER_RANK;j++) {
                cards[i][j] = new JToggleButton();
                cards[i][j].setBounds(offset+(250+150*j)*ratio/100, ratio/3+(200+225*i)*ratio/100, CARD_WIDTH,CARD_HEIGHT);
                gameArea.add(cards[i][j]);
            }
        }

        String defaultFont = new JButton().getFont().toString();
        collect = new JButton("Collect");
        collect.setBackground(new Color(202,145,66));
        collect.setFont(new Font(defaultFont,0, ratio/5));
        collect.setBounds(ratio*43/4, 5*ratio, ratio*5/4, ratio/2);
        gameArea.add(collect);

        reset = new JButton("Reset");
        reset.setBackground(new Color(202,145,66));
        reset.setFont(new Font(defaultFont,0, ratio/5));
        reset.setBounds(ratio*43/4, 6*ratio, ratio*5/4, ratio/2);
        gameArea.add(reset);

        buy = new JButton("Buy");
        buy.setBackground(new Color(202,145,66));
        buy.setFont(new Font(defaultFont,0, ratio/5));
        buy.setBounds(ratio*43/4, 7*ratio, ratio*5/4, ratio/2);
        gameArea.add(buy);

        reserve = new JButton("Reserve");
        reserve.setBackground(new Color(202,145,66));
        reserve.setFont(new Font(defaultFont,0, ratio/6));
        reserve.setBounds(ratio*43/4, 8*ratio, ratio*5/4, ratio/2);
        gameArea.add(reserve);
    }

    private void setPlayerArea() {

        playerArea.setLayout(new BoxLayout(playerArea,BoxLayout.Y_AXIS));
        players = new PlayerPanel[NUM_PLAYER];
        for (int i=0;i<NUM_PLAYER;i++){
            players[i] = new PlayerPanel(cardImages,gemImages);
            players[i].setPreferredSize(new Dimension(PLAYER_WIDTH,PLAYER_HEIGHT));
            playerArea.add(players[i]);
        }
    }

/*
    public static void main(String[] args) throws IOException {
        new BoardUI();
    }
*/
}
