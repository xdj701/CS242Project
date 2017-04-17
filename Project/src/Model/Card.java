package Model;

import Model.utils.GemInfo;

/**
 * Created by wu on 4/8/17.
 */
public class Card {
    final private int cardScore;
    final private GemInfo developmentCost;
    final private Gem targetGem;
    private boolean reserved;


    public Card(int score, GemInfo developmentCost, Gem targetGem) {
        this.cardScore = score;
        this.developmentCost = developmentCost;
        this.targetGem = targetGem;
        this.reserved = false;
    }

    public boolean isReserved(){
        return this.reserved;
    }

    public void setReserved(){
        this.reserved = true;
    }
    public Gem getTargetGem(){
        return targetGem;
    }

    public GemInfo getDevelopmentCost(){
        return developmentCost;
    }

    public int getCardScore(){
        return cardScore;
    }
}


