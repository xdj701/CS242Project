package Model;

import Model.utils.GemInfo;

import java.util.ArrayList;

/**
 * Created by wu on 4/8/17.
 */
public class Card {
    final private int cardScore;
    final private GemInfo developmentCost;
    final private Gem targetGem;
    private boolean reserved;
    private int rank;
    private int index;


    public Card(int score, GemInfo developmentCost, Gem targetGem) {
        this.cardScore = score;
        this.developmentCost = developmentCost;
        this.targetGem = targetGem;
        this.reserved = false;
    }

    public void setPosition(int rank, int index){
        this.rank = rank;
        this.index = index;
    }

    public int[] getPosition(){
        return new int[]{rank,index};
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


