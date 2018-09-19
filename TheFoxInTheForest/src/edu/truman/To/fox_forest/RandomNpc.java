package edu.truman.To.fox_forest;

import java.util.ArrayList;

/**
 * An Npc that always randomly chooses a card from the pool of eligible cards.
 * For the Fox and Woodcutter abilities, a random card is chosen to be discarded,
 * with the recently obtained card also included in the pool of cards to discard.
 * 
 * @author Chandler To
 *
 */

public class RandomNpc extends Npc {

	private ArrayList<Card> hand;
	
	/**
	 * Creates a RandomNpc with the given Game.
	 */
	public RandomNpc() {
		hand = new ArrayList<Card>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawHand(Deck deck, int handSize) {
		while (hand.size() < handSize) {
			hand.add(deck.draw());
		}
		sortHand();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sortHand() {
		for (int i = 0; i < hand.size(); i++) {
			int minIdx= 0;
			for (int j = 1; j < hand.size()-i; j++) {
				if (hand.get(minIdx).compareTo(hand.get(j)) > 0) {
					minIdx = j;
				}
			}
			hand.add(hand.remove(minIdx));
		}
	}
	
	/**
	 * Returns the NPC's strategy.
	 * For this NPC which only has a single strategy, 0 is always returned.
	 * 
	 * @return an integer showing this npc's strategy (always 0).
	 */
	public int getStrategy() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearHand(Deck deck) {
		while (hand.size() > 0) {
			deck.putBottom(hand.remove(0));
		}
	}
	
	/**
	 * Selects and removes any card in hand randomly.
	 * 
	 * @return the card the player is using.
	 */
	@Override
	public Card selectCardFirst() {
		return hand.remove((int) Math.random() * (hand.size()-1));
	}

	/**
	 * Selects and removes a card at random that matches suit with
	 * the given card, or any card if none match suit.
	 * 
	 * @param lead the card that is leading the trick. The player's
	 * 			   card must match suit if able.
	 * @return the card the player is using.
	 */
	@Override
	public Card selectCardSecond(Card lead) {
		if (lead.getValue() == Card.MONARCH) {
			boolean useOne = ((int) Math.random()) == 0? true : false;
			int lowIndex = lowestCardIndex(lead.getSuit());
			int highIndex = highestCardIndex(lead.getSuit());
			if (useOne && lowIndex != -1 && hand.get(lowIndex).getValue() == 1) {
				return hand.remove(lowIndex);
			}
			else if (highIndex != -1){
				return hand.remove(highIndex);
			}
		}
		
		else {
			int leadSuit = lead.getSuit();
			int low = lowestCardIndex(leadSuit);
			int high = highestCardIndex(leadSuit);
			if (low != -1) {
				return hand.remove((int) Math.random() * (high-low) + low);
			}
		}
	
		return hand.remove((int) Math.random() * (hand.size()-1));
		
	}
	
	/**
	 * Returns the index of the lowest-valued card matching
	 * the given suit, or -1 if no card in hand matches suit.
	 * 
	 * @param leadSuit the suit that is checked against
	 * @return the index of the lowest valued card matching suit
	 */
	private int lowestCardIndex(int leadSuit) {
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getSuit() == leadSuit) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the index of the highest-valued card matching
	 * the given suit, or -1 if no card in hand matches suit.
	 * 
	 * @param leadSuit the suit that is checked against
	 * @return the index of the highest valued card matching suit
	 */
	private int highestCardIndex(int leadSuit) {
		for (int i = hand.size()-1; i >= 0; i--) {
			if (hand.get(i).getSuit() == leadSuit) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Computer adds a card to hand, then randomly selects a card from 
	 * hand to discard.
	 * Used when the player has played a card with the "Fox" ability, which allows
	 * the player to switch the decree card with a card from hand.
	 * 
	 * @param current the card that is added to hand
	 * @return the card that is discarded
	 */
	@Override
	public Card swapDecreeCard(Card current) {
		int choice = (int) Math.random() * hand.size();
		if (choice == hand.size()) {
			return current;
		}
		else {
			Card newDecree = hand.remove(choice);
			hand.add(current);
			this.sortHand();
			return newDecree;
		}
	}

	/**
	 * Computer adds a card to hand, then randomly selects a card from 
	 * hand to discard. 
	 * Used when the player has played a card with the "Fox" ability, which allows
	 * the player to switch the decree card with a card from hand. 
	 * 
	 * @param drawn the card that is added to hand
	 * @return the card that is discarded
	 */
	@Override
	public Card drawAndDiscard(Card drawn) {
		int choice = (int) Math.random() * hand.size();
		if (choice == hand.size()) {
			return drawn;
		}
		else {
			Card newDecree = hand.remove(choice);
			hand.add(drawn);
			this.sortHand();
			return newDecree;
		}
	}

}
