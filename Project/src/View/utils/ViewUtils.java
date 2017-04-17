package View.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dajun on 4/17/17.
 */
public class ViewUtils {

    public final static int ratio = 70;

    public final static int WINDOW_WIDTH=20*ratio;
    public final static int WINDOW_HEIGHT=10*ratio;
    public final static int GAME_WIDTH=13*ratio;
    public final static int GAME_HEIGHT=10*ratio;
    public final static int PLAYER_WIDTH=7*ratio;
    public final static int PLAYER_HEIGHT=10*ratio;

    public final static int CARD_WIDTH = ratio*3/2;
    public final static int CARD_HEIGHT = ratio*2;
    public final static int GEM_WIDTH = ratio;
    public final static int GEM_HEIGHT = ratio;
    public final static int NOBLE_WIDTH = ratio*5/4;
    public final static int NOBLE_HEIGHT = ratio*5/4;


    public final static Color customWhite = new Color(246,247,248);
    public final static Color customBlack = new Color(66,51,52);
    public final static Color customGreen = new Color(63,193,83);
    public final static Color customRed = new Color(252,45,46);
    public final static Color customBlue = new Color(40,150,221);

    public final static ArrayList<Color> colorMap= new ArrayList<Color>(Arrays.asList(customWhite,customBlack,customGreen,customRed,customBlue));

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
        if(fillColor == customWhite)
            g2d.setColor(customBlack);
        else
            g2d.setColor(customWhite);
        g2d.drawOval(x,y,d,d);

    }

    //add a border line to Rectangle
    public static void plotRectWithOutline(Graphics2D g2d, Color fillColor, int x, int y,int w, int h){

        g2d.setColor(fillColor);
        g2d.fillRect(x,y,w,h);

        //if the color is white, use black as the border color
        if(fillColor == customWhite)
            g2d.setColor(customBlack);
        else
            g2d.setColor(customWhite);
        g2d.drawRect(x,y,w,h);

    }

}
