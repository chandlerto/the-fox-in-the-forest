package edu.truman.To.fox_forest;

import java.util.ArrayList;

import edu.truman.To.fox_forest.Card;
import edu.truman.To.fox_forest.Deck;
import edu.truman.To.fox_forest.Npc;

/**
 * A very simple NPC with some strategy.
 * 
 * @author Chandler To
 *
 */
public class SimpleNpc extends Npc {

	static final int[] cardRatings = {-8, -7, -4, -2, -1, 0, 2, 5, 10, 8, 10}; 
	
	private int[] numSuit;
	private boolean[][] haveCard;
	private ArrayList<Card> hand;
	
	private boolean aimingHigh;
	
	/**
	 * Creates a SimpleNpc with no cards.
	 */
	public SimpleNpc() {
		hand = new ArrayList<Card>();
		numSuit = new int[Card.SUITS.length];
		haveCard = new boolean[Card.SUITS.length][12];
	}
	
	/**
	 * Adds cards to the player's hand from the deck to reach the desired size.
	 * After cards are drawn, the cards are then sorted, then the hand is 
	 * evaluated to determine the computer's strategy. 
	 * 
	 * @param deck the deck the player is drawing from.
	 * @param handSize the amount of cards the player will have in hand.
	 * @precondition handSize + the player's current hand size <= deck's size
	 */
	@Override
	public void drawHand(Deck deck, int handSize) {
		while (hand.size() < handSize) {
			Card draw = deck.draw();
			hand.add(draw);
			haveCard[draw.getSuit()][draw.getValue()] = true;
			numSuit[draw.getSuit()]++;
		}
		sortHand();
		evaluateHand();
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
	 * Evaluates the value of the cards in the player's hand to 
	 * determine the strategy for the round.
	 */
	private void evaluateHand() {
		int winIndex = 0;
		for (Card card : hand) {
			winIndex += cardRatings[card.getValue()-1];
		}
		aimingHigh = winIndex > 0 ? true : false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card selectCardFirst() {
		
		/*
		 * If aiming high, play cards above 7 in ascending order.
		 * If none available, play the lowest card in hand. 
		 */
		if (aimingHigh) {
			for (int i = 8; i < haveCard[0].length; i++) {
				for (int j = 0; j < haveCard.length; j++) {
					if (haveCard[j][i]) {
						return removeCard(j, i);
					}
				}
			}
	
			return removeCard(0);
		}
		
		/*
		 * If aiming low, play card below 7 in descending order.
		 * If none available, play the highest card in hand.
		 */
		else {
			for (int i = 6; i > 0; i--) {
				for (int j = 0; j < haveCard.length; j++) {
					if (haveCard[j][i]) {
						return removeCard(j, i);
					}
				}
			}
			
			return removeCard(hand.size()-1);
		}
	}
	
	/**
	 * Used when removing a card from hand. The card of the 
	 * given suit and value is removed.
	 * @param suit the suit of the card being removed
	 * @param value the value of the card being removed
	 * @return the card being removed
	 */
	private Card removeCard(int suit, int value) {
		int idx = 0;
		if (suit == 1) {
			idx += numSuit[0];
		}
		else if (suit == 2) {
			idx += numSuit[0] + numSuit[1];
		}
		for (int i = idx; i < hand.size(); i++) {
			if (hand.get(i).getValue() == value) {
				idx = i;
				i = hand.size();
			}
		}
		
		Card card = hand.get(idx);
		haveCard[card.getSuit()][card.getValue()] = false;
		numSuit[card.getSuit()]--;
		return hand.remove(idx);
	}
	
	/**
	 * Used when removing a card from hand by index.
	 * @param idx the index of the card being removed
	 * @return the card being removed
	 */
	private Card removeCard(int idx) {
		Card card = hand.get(idx);
		haveCard[card.getSuit()][card.getValue()] = false;
		numSuit[card.getSuit()]--;
		return hand.remove(idx);
	}

	@Override
	public Card selectCardSecond(Card lead) {
		int leadValue = lead.getValue();
		int leadSuit = lead.getSuit();
		int decreeSuit = game.getDecreeCard().getSuit();
		
		if (numSuit[leadSuit] > 0) {
			
			if (leadValue == Card.MONARCH) {
				
				/*
				 * If the Npc doesn't have the 1, it plays its highest card
				 * as required by the Monarch ability.
				 */
				if (haveCard[leadSuit][0] == false) {
					for (int i = haveCard[leadSuit].length-1; i >= 0; i--) {
						if (haveCard[leadSuit][i])
							return removeCard(leadSuit, i);
					}
				}
				
				else {
					int highestValue = highestCard(leadSuit);
					if (aimingHigh) {
						/*
						 * If aiming high, the npc loses with the 1 unless it can
						 * win by a Witch effect.
						 */
						if (highestValue == Card.WITCH && leadSuit != decreeSuit)
							return removeCard(leadSuit, Card.WITCH);
						else
							return removeCard(leadSuit, 1); 
					}
					else {
						/*
						 * If aiming low, the npc plays highest card.
						 */
						return removeCard(leadSuit, highestValue);
					}
					
				}
			}
			
			else if (aimingHigh) {
				/*
				 * Tries to win with the lowest card possible.
				 */
				for (int i = leadValue+1; i < haveCard[leadSuit].length; i++) {
					if (haveCard[leadSuit][i]) {
						return removeCard(leadSuit, i);
					}
				}
				/*
				 * Unable to win, so try to lose with lowest card.
				 */
				for (int i = 0; i < haveCard[leadSuit].length; i++) {
					if (haveCard[leadSuit][i]) {
						return removeCard(leadSuit, i);
					}
				}
			}
			
			else {
				/*
				 * Tries to lose with highest card possible
				 */
				for (int i = leadValue-1; i > 0; i--) {
					if (haveCard[leadSuit][i]) {
						return removeCard(leadSuit, i);
					}
				}
				/*
				 * Unable to lose, plays highest card.
				 */
				for (int i = Card.MAX; i > 0; i--) {
					if (haveCard[leadSuit][i]) {
						return removeCard(leadSuit, i);
					}
				}
			}
		}
		
		else {
			
			/*
			 * If opponent is in high suit and npc is aiming high,
			 * a witch will be used to win if possible, or the 
			 * npc will lose with its lowest card.
			 */
			if (leadSuit == decreeSuit && aimingHigh) {
				if (leadValue < Card.WITCH) {
					for (int i = 0; i < haveCard.length; i++) {
						if (haveCard[i][Card.WITCH]) {
							return removeCard(i, Card.WITCH);
						}
					}
				}
				
				return removeCard(0);
			}
			
			/*
			 * If opponent isn't in high suit and npc is aiming high,
			 * then either a card of the high suit or a higher valued 
			 * card will be played, or a Witch if able, 
			 * or the lowest card if unable to win. 
			 */
			else if (leadSuit != decreeSuit && aimingHigh) {
				for (int i = 0; i < haveCard[0].length; i++) {
					for (int j = 0; j < haveCard.length; j++) {
						if (haveCard[j][i] 
								&& (j == decreeSuit || i > leadValue))
							return removeCard(j, i);
					}
				}
				for (int i = 0; i < haveCard.length; i++) {
					if (haveCard[i][Card.WITCH])
						return removeCard(i, Card.WITCH);
				}
				return removeCard(0);
			}
			
			/*
			 * If the opponent is in the high suit and npc is aiming low,
			 * the highest card in hand is played. A Witch is only played 
			 * if it will lose. 
			 */
			else if (leadSuit == decreeSuit && !aimingHigh) {
				for (int i = Card.MAX; i > Card.WITCH; i--) {
					for (int j = 0; j < haveCard.length; j++) {
						if (haveCard[j][i]) {
							return removeCard(j, i);
						}
					}
				}
				for (int j = 0; j < haveCard.length; j++) {
					if (haveCard[j][Card.WITCH] 
							&& leadValue > Card.WITCH) {
						return removeCard(j, Card.WITCH);
					}
				}
				for (int i = Card.WITCH-1; i > 0; i--) {
					for (int j = 0; j < haveCard.length; j++) {
						if (haveCard[j][i]) {
							return removeCard(j, i);
						}
					}
				}
				return removeCard(hand.size()-1);
			}
			
			/*
			 * If opponent is not high suit and npc is aiming low,
			 * The npc will try to lose with a card of the other 
			 * not high suit. If unable to, it will try to win with 
			 * the highest card of the high suit. If none available,
			 * the highest card in hand is played.
			 */
			else {
				int other = 3 - leadSuit - decreeSuit;
				for (int i = leadValue; i > 0; i--) {
					if (haveCard[other][i] && i != Card.WITCH) {
						return removeCard(other, i);
					}
				}
				if (numSuit[decreeSuit] > 0) {
					for (int i = Card.MAX; i > 0; i--) {
						if (haveCard[decreeSuit][i]) {
							return removeCard(decreeSuit, i);
						}
					}
				}
				return removeCard(hand.size()-1);
			}	
		}
		return removeCard(0);
	}

	/**
	 * Returns the value of the highest card of the give suit.
	 * If no card exists of that suit, 0 is returned.
	 * @param suit the suit that is being searched
	 * @return the value of the highest card of the given suit, or 0 if none.
	 */
	private int highestCard(int suit) {
		for (int i = Card.MAX; i > 0; i--) {
			if (haveCard[suit][i])
				return i;
		}
		return 0;
	}
	
	@Override
	public Card swapDecreeCard(Card current) {
		return current;
	}

	@Override
	public Card drawAndDiscard(Card drawn) {
		return drawn;
	}

}
