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
import java.util.Hashtable;

import static Model.utils.GameUtils.*;
import static View.utils.ViewUtils.*;
import Game.Game;
import Model.*;
import Model.utils.GemInfo;

/**
 * Created by Yu on 4/16/17.
 */
public class BoardUI {

    private JFrame window;
    private JPanel gameArea;
    private JPanel playerArea;

    private JPanel decks[];
    private JPanel nobles[];
    private JButton gold;
    private JButton gems[];
    private JButton cards[][];
    private PlayerPanel players[];
    private JButton collect, reset, buy, reserve;

    private JMenuItem newGame;
    private JMenuItem exit;

    private Hashtable<String, Image> cardImages = new Hashtable<String, Image>();
    private Hashtable<String, Image> gemImages = new Hashtable<String, Image>();
    private Hashtable<String, Image> nobleImages = new Hashtable<String, Image>();
    Image backgroundImage;

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

    public BoardUI(Game game) {

        loadImagesInMemory();
        window = new JFrame("Splendor");
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
            }
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

    public JButton[] getGems(){
        return this.gems;
    }

    public JButton getGold(){
        return this.gold;
    }

    public JButton[][] getCards(){
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

        playerArea = new JPanel();
        playerArea.setPreferredSize(new Dimension(PLAYER_WIDTH,PLAYER_HEIGHT));
        setPlayerArea();
        window.add(playerArea,BorderLayout.EAST);
    }

    public Gem getGemByIndex(int id){
        switch (id){
            case 1: return Gem.DIAMOND;
            case 2: return Gem.EMERALD;
            case 3: return Gem.ONYX;
            case 4: return Gem.RUBY;
            case 5: return Gem.SAPPHIRE;
            default: return null;
        }
    }

    private void setGameBoard() {

        int offset = ratio * 2;
        nobles = new JPanel[NUM_PLAYER+1];
        for (int i=0;i<NUM_PLAYER+1;i++){
            //nobles[i] = new JPanel(new BorderLayout());

            Noble nobleInput = new Noble(0,0,3,3,3);
            nobles[i] = new NoblePanel(nobleInput,nobleImages);

            nobles[i].setBounds(offset+(NOBLE_WIDTH*2) * (i+1) *ratio/100, 50*ratio/100, NOBLE_WIDTH,NOBLE_HEIGHT);
            nobles[i].setBackground(Color.black);
            gameArea.add(nobles[i]);
        }

        //gold = new JButton("gold");
        gold = new GoldButton(5,gemImages);
        gold.setBounds(75*ratio/100,200*ratio/100-50, GEM_WIDTH, GEM_HEIGHT);

        gameArea.add(gold);
        gems = new JButton[5];
        for (int i=0;i<5;i++) {
            //gems[i] = new JButton("tmp");
            gems[i] = new GemButton(getGemByIndex(5-i),7,gemImages);
            gems[i].setBounds(75*ratio/100, (775-125*i)*ratio/100, GEM_WIDTH, GEM_HEIGHT);
            gameArea.add(gems[i]);
        }

        decks = new JPanel[NUM_CARD_RANK];
        for(int i = 0 ; i< NUM_CARD_RANK; i++){
            decks[i] = new JPanel();
            //load image
            decks[i].setBounds(offset+ratio, (200+225*i)*ratio/100, CARD_WIDTH,CARD_HEIGHT);
            decks[i].setBackground(Color.orange);
            gameArea.add(decks[i]);
        }


        cards = new JButton[NUM_CARD_RANK][NUM_CARD_PER_RANK];
        for (int i=0;i<NUM_CARD_RANK;i++){
            for (int j=0;j<NUM_CARD_PER_RANK;j++) {
                //cards[i][j] = new JButton();
                GemInfo developmentCost = new GemInfo(1,1,0,1,1);
                Card card = new Card(1,developmentCost,Gem.DIAMOND);
                cards[i][j] = new CardButton(card,gemImages,cardImages);
                cards[i][j].setBounds(offset+(250+150*j)*ratio/100, (200+225*i)*ratio/100, CARD_WIDTH,CARD_HEIGHT);
                gameArea.add(cards[i][j]);
            }
        }

        collect = new JButton("Collect");
        collect.setBounds(11*ratio, 2*ratio, ratio, ratio);
        gameArea.add(collect);

        reset = new JButton("Reset");
        reset.setBounds(11*ratio, 3*ratio, ratio, ratio);
        gameArea.add(reset);

        buy = new JButton("Buy");
        buy.setBounds(11*ratio, 4*ratio, ratio, ratio);
        gameArea.add(buy);

        reserve = new JButton("Reserve");
        reserve.setBounds(11*ratio, 5*ratio, ratio, ratio);
        gameArea.add(reserve);
    }

    private void setPlayerArea() {

        playerArea.setLayout(new BoxLayout(playerArea,BoxLayout.Y_AXIS));
        players = new PlayerPanel[NUM_PLAYER];
        for (int i=0;i<NUM_PLAYER;i++){
            players[i] = new PlayerPanel();
            players[i].setPreferredSize(new Dimension(7*ratio,9*ratio/NUM_PLAYER));

            playerArea.add(players[i]);
        }
    }

    public class PlayerPanel extends JPanel{

        //need the input of controller
        JPanel info;
        JButton reservedCards[];
        JPanel status;
        JPanel gems;
        JPanel cards;

        private PlayerPanel() {

            //need input of controller
            info = new JPanel();
            info.setLayout(new BoxLayout(info , BoxLayout.Y_AXIS));
            setStatus(1,5,true);
            setGems(new GemInfo(0),0);
            setCards(new GemInfo(0));

            info.add(status);
            info.add(gems);
            info.add(cards);

            this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
            this.add(info);

            reservedCards = new JButton[3];
            for (int i=0;i<3;i++){
                reservedCards[i] = new JButton();
                reservedCards[i].setPreferredSize(new Dimension(75, 100));
                this.add(reservedCards[i]);
            }
        }

        private PlayerPanel(Player player,boolean current) {

            //need input of controller
            setStatus(player.getId(),player.getScore(),current);
            setGems(new GemInfo(0),0);
            setCards(new GemInfo(0));

            this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            this.add(status);
            this.add(gems);
            this.add(cards);
        }


        private void setStatus(int id, int score, boolean indicator){

            int width = ratio;
            int height = ratio*3/NUM_PLAYER;

            status = new JPanel();
            status.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));

            JLabel idLabel = new JLabel(Integer.toString(id),JLabel.CENTER);
            idLabel.setPreferredSize(new Dimension(width,height));
            status.add(idLabel);

            JLabel scoreLabel = new JLabel(Integer.toString(score),JLabel.CENTER);
            scoreLabel.setPreferredSize(new Dimension(width,height));
            status.add(scoreLabel);

            JLabel indicatorLabel;
            BufferedImage buffered = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffered.createGraphics();
            if(indicator) {
                g2d.setColor(Color.GREEN);
            }
            else
                g2d.setColor(Color.RED);
            g2d.fillOval(width*3/8,height*3/8,width/4,width/4);
            ImageIcon icon= new ImageIcon(buffered);
            indicatorLabel = new JLabel(icon, JLabel.CENTER);
            indicatorLabel.setPreferredSize(new Dimension(ratio,ratio*3/NUM_PLAYER));
            status.add(indicatorLabel);
        }


        private void setGems(GemInfo gemInfo, int gold){
            gems = new JPanel();
            gems.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            for(int i = 1; i <= 5; i++){
                int gem = gemInfo.getByIndex(i);
                JLabel gemLabel = new JLabel(Integer.toString(gem),JLabel.CENTER);
                gemLabel.setPreferredSize(new Dimension(ratio/2,ratio*3/NUM_PLAYER));
                gems.add(gemLabel);
            }
            JLabel goldLabel = new JLabel(Integer.toString(gold),JLabel.CENTER);
            goldLabel.setPreferredSize(new Dimension(ratio/2,ratio*3/NUM_PLAYER));
            gems.add(goldLabel);
            gems.add(goldLabel);
        }


        private void setCards(GemInfo gemInfo){
            cards = new JPanel();
            cards.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            for(int i = 1; i <= 5; i++){
                int bonus = gemInfo.getByIndex(i);
                JLabel bonusLabel = new JLabel(Integer.toString(bonus),JLabel.CENTER);
                bonusLabel.setPreferredSize(new Dimension(ratio*3/5,ratio*3/NUM_PLAYER));
                cards.add(bonusLabel);
            }
        }

        public JButton[] getReservedCards(){
            return this.reservedCards;
        }

    }



    public static void main(String[] args) throws IOException {
        new BoardUI();
    }
}