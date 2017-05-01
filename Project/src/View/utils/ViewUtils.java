package View.utils;

import Model.Card;
import Model.Gem;
import Model.utils.GemInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import static Model.utils.GameUtils.NUM_PLAYER;

/**
 * Created by dajun on 4/17/17.
 */
public class ViewUtils {

    public final static int ratio = 70;


    public final static int CARD_WIDTH = ratio*3/2;
    public final static int CARD_HEIGHT = ratio*2;
    public final static int GEM_WIDTH = ratio;
    public final static int GEM_HEIGHT = ratio;
    public final static int NOBLE_WIDTH = ratio*5/4;
    public final static int NOBLE_HEIGHT = ratio*5/4;


    public final static int GAME_WIDTH=13*ratio;
    public final static int GAME_HEIGHT=10*ratio;
    public final static int PLAYER_WIDTH=3*ratio+3*CARD_WIDTH;
    public final static int PLAYER_HEIGHT=GAME_HEIGHT/NUM_PLAYER;
    public final static int WINDOW_WIDTH=GAME_WIDTH+PLAYER_WIDTH;
    public final static int WINDOW_HEIGHT=GAME_HEIGHT;

    public final static Color CUSTOM_WHITE = new Color(246,247,248);
    public final static Color CUSTOM_BLACK = new Color(66,51,52);
    public final static Color CUSTOM_GREEN = new Color(63,193,83);
    public final static Color CUSTOM_RED = new Color(252,45,46);
    public final static Color CUSTOM_BLUE = new Color(40,150,221);

    public final static ArrayList<Color> COLOR_MAP= new ArrayList<Color>(Arrays.asList(CUSTOM_WHITE,CUSTOM_GREEN,CUSTOM_BLACK,CUSTOM_RED,CUSTOM_BLUE));

    //Set the top part of image to translucent
    public static void addOpaqueLayer(BufferedImage img){
        //50% opacity
        int alpha = 127;
        for(int cx = 0; cx<img.getWidth();cx++ ){
            for(int cy = 0; cy < img.getHeight(); cy++){
                //ARGB(8 bits per channel)
                int color = (alpha << 24) + img.getRGB(cx, cy);
                img.setRGB(cx,cy,color);
            }
        }
    }


    //add a border line to string
    public static void plotStringWithOutline(Graphics2D g2d, String s, int x, int y, int size){
        Font font = new Font("SansSerif",  Font.ITALIC + Font.BOLD, size);
        g2d.setFont(font);
        g2d.setColor(Color.black);
        g2d.drawString(s,x-1,y-1);
        g2d.drawString(s,x-1,y+1);
        g2d.drawString(s,x+1,y-1);
        g2d.drawString(s,x+1,y+1);
        g2d.setColor(Color.white);
        g2d.drawString(s,x,y);
    }


    //add a border line to Circle
    public static void plotCircleWithOutline(Graphics2D g2d, Color fillColor, int x, int y,int d){

        g2d.setColor(fillColor);
        g2d.fillOval(x,y,d,d);
        //if the color is white, use black as the border color
        if(fillColor == CUSTOM_WHITE)
            g2d.setColor(CUSTOM_BLACK);
        else
            g2d.setColor(CUSTOM_WHITE);
        g2d.drawOval(x,y,d,d);

    }

    //add a border line to Rectangle
    public static void plotRectWithOutline(Graphics2D g2d, Color fillColor, int x, int y,int w, int h){

        g2d.setColor(fillColor);
        g2d.fillRect(x,y,w,h);

        //if the color is white, use black as the border color
        if(fillColor == CUSTOM_WHITE)
            g2d.setColor(CUSTOM_BLACK);
        else
            g2d.setColor(CUSTOM_WHITE);
        g2d.drawRect(x,y,w,h);

    }

    //paint the border of cards
    public static void highlightButton(Graphics2D g2d, int w, int h){
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.orange);
        g2d.drawRect(0,0,w,h);
        g2d.setStroke(oldStroke);
    }


    //fill the background of gem buttons if it is selected
    public static ImageIcon plotGemButton(Gem gem, int left, Hashtable<String, Image> gemImages,int width, boolean selected){

        BufferedImage buffered = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();
        int r = width/2;

        if(selected){
            g2d.setColor(Color.orange);
            g2d.fillOval(0,0,2*r,2*r);
        }

        //plot gem and resize the image
        Image gemImg = gemImages.get(gem.getGemName()).getScaledInstance(width,width,Image.SCALE_SMOOTH);
        g2d.drawImage(gemImg, 0, 0 , null);
        plotStringWithOutline(g2d,Integer.toString(left),r/2,r*3/2,r*3/4);

        return new ImageIcon(buffered);
    }


    //plot gold button with subscript number indicating the remaining gold
    public static ImageIcon plotGoldButton(int left, Hashtable<String, Image> gemImages,int width, boolean selected){

        BufferedImage buffered = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();
        int r = width/2;

        if(selected){
            g2d.setColor(Color.orange);
            g2d.fillOval(0,0,2*r,2*r);
        }

        //plot gold and resize the image
        Image gemImg = gemImages.get("Gold").getScaledInstance(width,width,Image.SCALE_SMOOTH);;
        g2d.drawImage(gemImg, 0,0, null);

        plotStringWithOutline(g2d,Integer.toString(left),r/2,r*3/2,r*3/4);

        return new ImageIcon(buffered);
    }

    //
    public static ImageIcon plotCardButton(Card card, Hashtable<String, Image> gemImages, Hashtable<String, Image> cardImages, boolean selected) {

        //extract the card information
        Gem gem = card.getTargetGem();
        GemInfo gemInfo = card.getDevelopmentCost();
        int score = card.getCardScore();

        BufferedImage buffered = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffered.createGraphics();

        //plot background card
        Image backImg = cardImages.get(gem.getGemName());
        g2d.drawImage(backImg, 0, 0 , null);

        //Reduce the opacity to 50% of the card's top part
        BufferedImage subImage = buffered.getSubimage(0, 0, CARD_WIDTH, CARD_HEIGHT/5); // x, y, width, height
        addOpaqueLayer(subImage);

        //plot score
        if(score != 0){
            plotStringWithOutline(g2d,Integer.toString(score),CARD_WIDTH/10,CARD_HEIGHT*15/100,CARD_WIDTH/5);
        }

        //plot targetGem
        Image bonusImg = gemImages.get(gem.getGemName());
        bonusImg = bonusImg.getScaledInstance(GEM_WIDTH/2,GEM_HEIGHT/2,Image.SCALE_SMOOTH);
        g2d.drawImage(bonusImg,CARD_WIDTH*7/10,0,null);

        //plot development cost
        int x = CARD_WIDTH/20;
        int r = CARD_WIDTH/10;
        int idx = 1;
        for(int i = 1; i <= 5; i++){
            int cost = gemInfo.getByIndex(i);
            if(cost == 0)
                continue;
            int y = CARD_HEIGHT-idx*CARD_HEIGHT/6;
            idx++;
            plotCircleWithOutline(g2d,COLOR_MAP.get(i-1),x,y,2*r);
            plotStringWithOutline(g2d,Integer.toString(cost),x+r/2,y+r*3/2,r*3/2);
        }

        if(selected){
            highlightButton(g2d,CARD_WIDTH,CARD_HEIGHT);
        }


        return new ImageIcon(buffered);
    }

    public static Gem getGemByIndex(int id){
        switch (id){
            case 1: return Gem.DIAMOND;
            case 2: return Gem.EMERALD;
            case 3: return Gem.ONYX;
            case 4: return Gem.RUBY;
            case 5: return Gem.SAPPHIRE;
            default: return null;
        }
    }

}
