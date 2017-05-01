package View;

import Model.Card;
import Model.Player;
import Model.utils.GemInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import static Model.utils.GameUtils.NUM_PLAYER;
import static View.utils.ViewUtils.*;
import static View.utils.ViewUtils.getGemByIndex;

/**
 * Created by dajun on 4/18/17.
 */

public class PlayerPanel extends JPanel {

    //need the input of controller
    JPanel info;
    JButton reservedCards[];
    JPanel status;
    JPanel gems;
    JPanel cards;
    Color backColor = new Color(251,229,182);
    private Hashtable<String, Image> cardImages;
    private Hashtable<String, Image> gemImages;

    public PlayerPanel(Hashtable<String, Image> cardImg, Hashtable<String, Image> gemImg) {


        cardImages = cardImg;
        gemImages = gemImg;
        //this.setBackground(backColor);
        this.setOpaque(false);

        info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info , BoxLayout.Y_AXIS));

        status = new JPanel();
        status.setOpaque(false);
        status.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        //setStatus(1,5,true);

        gems = new JPanel();
        gems.setOpaque(false);
        gems.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        //setGems(new GemInfo(0),0);

        cards = new JPanel();
        cards.setOpaque(false);
        cards.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        //setCards(new GemInfo(0));

        info.add(status);
        info.add(gems);
        info.add(cards);

        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.add(info);

        reservedCards = new JButton[3];
        for (int i=0;i<3;i++){
            reservedCards[i] = new JButton();
            reservedCards[i].setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            this.add(reservedCards[i]);
        }
    }

    public void setStatus(String name, int score, boolean indicator){

        int width = PLAYER_WIDTH/9;
        int height = PLAYER_HEIGHT/3;
        status.setBackground(backColor);
        //status.setOpaque(false);

        status.removeAll();
        status.validate();
        status.repaint();
        JLabel idLabel = new JLabel(name,JLabel.CENTER);
        idLabel.setPreferredSize(new Dimension(width,height));
        status.add(idLabel);

        JLabel scoreLabel = new JLabel("Score: "+Integer.toString(score),JLabel.CENTER);
        scoreLabel.setPreferredSize(new Dimension(width*3/2,height));
        status.add(scoreLabel);

        JLabel indicatorLabel;
        BufferedImage buffered = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();
        if(indicator) {
            g2d.setColor(Color.GREEN);
        }
        else
            g2d.setColor(Color.RED);
        g2d.fillOval(width*4/8,height*3/8,width/4,width/4);
        ImageIcon icon= new ImageIcon(buffered);
        indicatorLabel = new JLabel(icon, JLabel.CENTER);
        indicatorLabel.setPreferredSize(new Dimension(width/2,height));
        status.add(indicatorLabel);
        status.validate();
        status.repaint();
    }


    public void setGems(GemInfo gemInfo, int gold){

        int width = (PLAYER_WIDTH-3*CARD_WIDTH)/6;
        int height = PLAYER_HEIGHT/3;

        gems.setBackground(backColor);
        gems.removeAll();
        gems.validate();
        gems.repaint();
        for(int i = 1; i <= 5; i++){
            int gem = gemInfo.getByIndex(i);
            JLabel gemLabel = new JLabel();
            gemLabel.setIcon(plotGemButton(getGemByIndex(i),gem,gemImages,width,false));
            gemLabel.setPreferredSize(new Dimension(width,height));
            gems.add(gemLabel);
        }
        //JLabel goldLabel = new JLabel(Integer.toString(gold),JLabel.CENTER);
        JLabel goldLabel = new JLabel();
        goldLabel.setIcon(plotGoldButton(gold,gemImages,width,false));
        goldLabel.setPreferredSize(new Dimension(width,height));
        gems.add(goldLabel);
        gems.validate();
        gems.repaint();
    }

    public BufferedImage displayCards(int i, int bonus, int width, int height){
        Color fillColor = Color.ORANGE;
        Color fontColor = Color.WHITE;
        switch (i){
            case 1: fillColor = CUSTOM_WHITE;fontColor=Color.BLACK;break;
            case 2: fillColor = CUSTOM_GREEN;break;
            case 3: fillColor = CUSTOM_BLACK;break;
            case 4: fillColor = CUSTOM_RED;break;
            case 5: fillColor = CUSTOM_BLUE;break;
            default: break;
        }

        BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();
        g2d.setColor(fillColor);
        g2d.fillRect(0,0,width,height);

        Font font = new Font("SansSerif",  Font.ITALIC + Font.BOLD, width/2);
        g2d.setFont(font);
        g2d.setColor(fontColor);
        g2d.drawString(Integer.toString(bonus),width*3/8,height/2);
        return buffered;
    }

    public void setCards(GemInfo gemInfo){

        int width = (PLAYER_WIDTH-3*CARD_WIDTH)/5;
        int height = PLAYER_HEIGHT/3;

        cards.setBackground(backColor);
        cards.removeAll();
        cards.validate();
        cards.repaint();


        for(int i = 1; i <= 5; i++){

            int bonus = gemInfo.getByIndex(i);
            BufferedImage buffered = displayCards(i, bonus, width,height);
            JLabel bonusLabel = new JLabel(new ImageIcon(buffered),JLabel.CENTER);
            bonusLabel.setPreferredSize(new Dimension(width,height));
            cards.add(bonusLabel);
        }
        cards.validate();
        cards.repaint();
    }

    public void setReservedCards(ArrayList<Card> reserved, Hashtable<String, Image> gemImages, Hashtable<String, Image> cardImages){
        for (int i=0;i<3;i++) {
            reservedCards[i].removeAll();
            reservedCards[i].setOpaque(false);
            reservedCards[i].setContentAreaFilled(false);
            reservedCards[i].setBorderPainted(false);
            //if there is a reserved card, display the image
            if(i < reserved.size())
                reservedCards[i].setIcon(plotCardButton(reserved.get(i),gemImages,cardImages,false));
            else
                reservedCards[i].setIcon(null);
            reservedCards[i].validate();
            reservedCards[i].repaint();
        }
    }


    public JButton[] getReservedCards(){
        return this.reservedCards;
    }

}
