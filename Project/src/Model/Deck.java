package Model;
import java.util.ArrayList;

/**
 * Created by yu on 4/8/17.
 */
public class Deck {
    ArrayList<Card> low;
    ArrayList<Card> med;
    ArrayList<Card> high;


    public Deck() {
        low = new ArrayList<Card>();
        med = new ArrayList<Card>();
        high = new ArrayList<Card>();
    }
}
