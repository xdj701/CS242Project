package Model;
import Model.utils.*;
import java.util.ArrayList;

import static Model.utils.GameUtils.*;
import static Model.utils.GemInfo.*;
import static java.lang.Math.*;

/**
 * Created by wu on 4/8/17.
 */
public class Player {
	final private int id;
	private int score;
	private GemInfo gems;
	int golds;
	GemInfo cards;
	int numCards;
	ArrayList<Card> reserves;
	private Board board;

	public Player(int pid, Board newBoard){
		id = pid;
		score = 0;
		gems = new GemInfo(0);
		golds = 0;
		cards = new GemInfo(0);
		numCards = 0;
		reserves = new ArrayList<Card>();
		board = newBoard;
	}

	public int getId(){return id;}
	public int getScore() {return score;}
	public GemInfo getGems() {
		return gems;
	}
	public int getGolds() {
		return golds;
	}
	public GemInfo getCards() {
		return cards;
	}
	public int getNumCards() {
		return numCards;
	}
	public ArrayList<Card> getReserves() {
		return reserves;
	}


	public void updateScore(int newScore){ score = newScore;}



	/**
	 * add a new card to the player's own cards
	 * @param newCard
	 */
	public void addNewCard(Card newCard){
		Gem gemType = newCard.getTargetGem();
		String gemName = gemType.getGemName();
		switch (gemName) {
			case "Diamond":
				cards.diamond+=1;
				break;
			case "Emerald":
				cards.emerald+=1;
				break;
			case "Onyx":
				cards.onyx+=1;
				break;
			case "Ruby":
				cards.ruby+=1;
				break;
			case "Sapphire":
				cards.sapphire+=1;
				break;
		}
		this.score += newCard.getCardScore();
	}


	/**
	 * Buy a new card
	 * @param newCard
	 * @param isReserved whether the card is reserved or not
	 * @return whether the player can buy the card
	 */
	public boolean buyCard(Card newCard, boolean isReserved){
		GemInfo requiredGem = newCard.getDevelopmentCost();
		GemInfo restGem = newCard.getDevelopmentCost();
		//requiredGem is the gem we need to pay for this card
		reduceGems(requiredGem,this.cards);
		//restGem is the number of gems we still need to buy this card when we spend all of our gems
		reduceGems(restGem, this.cards);
		reduceGems(restGem,this.gems);

		//if the restGem is negative, then we have enough this kind of gem, so set it to 0.
		restGem.setGems(max(restGem.diamond, 0), max(restGem.emerald, 0), max(restGem.onyx, 0), max(restGem.ruby, 0), max(restGem.sapphire, 0));
		requiredGem.setGems(max(requiredGem.diamond, 0), max(requiredGem.emerald, 0), max(requiredGem.onyx, 0), max(requiredGem.ruby, 0), max(requiredGem.sapphire, 0));

		//if the restGem is smaller than the number of golds, we can use golds to pay the rest gems.
		if( restGem.diamond + restGem.emerald + restGem.onyx + restGem.ruby + restGem.sapphire <= golds ) {
			reduceGems(this.gems, requiredGem);
			this.golds -= (restGem.diamond + restGem.emerald + restGem.onyx + restGem.ruby + restGem.sapphire);
			board.changeGold(restGem.diamond + restGem.emerald + restGem.onyx + restGem.ruby + restGem.sapphire);
			board.changeGem(requiredGem);
			this.gems.setGems(max(this.gems.diamond, 0), max(this.gems.emerald, 0), max(this.gems.onyx, 0), max(this.gems.ruby, 0), max(this.gems.sapphire, 0));
			if(isReserved) {
				reserves.remove(newCard);
				addNewCard(newCard);
				return true;
			}
			else {
				addNewCard(newCard);
				return true;
			}
		}
		return false;
	}

	/**
	 * Reserved a specified card in this round by the player.
	 * @param newCard the card chose to be reserved
	 */
	public boolean reserveCard(Card newCard){
		//exceed the limit for reservation !
		if(this.reserves.size()>= MAX_RESERVED_CARDS){
			//TODO: Exception
			return false;
		}
		//no more gold available
		else if(this.board.getAvailableGolds()<=0) {
			return false;
		}
		//exceed the limit of gems you can hold
		else if(gems.GemTotalNum()+golds >= MAX_HOLD_GEMS){
			return false;
		}
		else{
		    newCard.setReserved();
			this.reserves.add(newCard);
			this.golds++;
			this.board.reduceAvailableGolds();
			return true;
		}
	}


	/**
	 * Update the gems of the current player according to the collection in this round.
	 * @param collectedGems The gems collected by player in this round
	 */
	public boolean collectGems(GemInfo collectedGems){
		//you can select at most three gems at a time
		if(collectedGems.GemTotalNum() > MAX_COLLECT_GEMS)
			return false;

		//you can select 3 distinct gems
		if(collectedGems.GemTotalNum() == MAX_COLLECT_GEMS){
			if(collectedGems.GemMaxTypeNum() >= MAX_SAME_TYPE_GEMS)
				return false;
		}

		//or two same gems if there are more than 4 gems of that type on the board
		if(collectedGems.GemTotalNum() == MAX_SAME_TYPE_GEMS){
			if(collectedGems.diamond == MAX_SAME_TYPE_GEMS) {
				if (this.board.availableGem.diamond < MIN_SAME_TYPE_GEMS_ON_BOARD)
					return false;
			}
			else if(collectedGems.emerald == MAX_SAME_TYPE_GEMS) {
				if (this.board.availableGem.emerald < MIN_SAME_TYPE_GEMS_ON_BOARD)
					return false;
			}
			else if(collectedGems.onyx == MAX_SAME_TYPE_GEMS) {
				if (this.board.availableGem.onyx < MIN_SAME_TYPE_GEMS_ON_BOARD)
					return false;
			}
			else if(collectedGems.ruby == MAX_SAME_TYPE_GEMS) {
				if (this.board.availableGem.ruby < MIN_SAME_TYPE_GEMS_ON_BOARD)
					return false;
			}
			else{
				if (this.board.availableGem.sapphire < MIN_SAME_TYPE_GEMS_ON_BOARD)
					return false;
			}
		}

		combineGems(this.gems, collectedGems);
		reduceGems(this.board.availableGem, collectedGems);
		return true;
	}

    public void recruitAvailableNobles(){
	    for(Noble noble : this.board.getNobles()){
            if(noble.satisfied(this.cards) && noble.getIsRecruited() == false){
                noble.beRecruited();
                this.score += noble.getScore();
            }
        }
    }



	/**
	 * Check if the player has won
	 * @return true if the player won
	 */
	public final boolean hasWon(){
		return this.score >= WINNING_SCORE;
	}

}


