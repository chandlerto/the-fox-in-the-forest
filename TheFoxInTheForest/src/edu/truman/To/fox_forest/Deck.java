package edu.truman.To.fox_forest;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Represents the deck containing the cards not in play nor in a player's hand.
 * 
 * @author Chandler To
 *
 */

public class Deck {

	private LinkedList<Card> cards;
	
	/**
	 * Creates a deck containing 33 unique cards in randomized order.
	 * 3 sets of each suit, containing cards from 1 to 11.
	 */
	public Deck() {
		cards = new LinkedList<Card>();
		for (int i = 1; i <= Card.MAX; i++) {
			for (int j = 0; j < Card.SUITS.length; j++) {
				cards.add(new Card(i, j));
			}
		}
		shuffle();
	}
	
	/**
	 * Randomizes the order of the cards in the deck.
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
	 * Removes and returns the top card of the deck.
	 * 
	 * @return the top card of the deck.
	 */
	public Card draw() {
		return cards.removeFirst();
	}
	
	/**
	 * Adds the given card to the bottom of the deck.
	 * 
	 * @param discard the card to be added to the bottom of the deck.
	 */
	public void putBottom(Card discard) {
		cards.addLast(discard);
	}
	
}
