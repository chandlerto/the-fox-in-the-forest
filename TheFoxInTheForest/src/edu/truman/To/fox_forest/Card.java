package edu.truman.To.fox_forest;
/**
 * Represents a card in the game.
 * 
 * @author Chandler To
 *
 */
public class Card {
	
	final static int SWAN_VALUE = 1;
	final static int FOX_VALUE = 3;
	final static int WOODCUTTER_VALUE = 5;
	final static int TREASURE_VALUE = 7;
	final static int WITCH_VALUE = 9;
	final static int MONARCH_VALUE = 11;
	final static String[] SUITS = {"Bells", "Keys", "Moons"};
	
	private int value;
	private int suit;
	
	/**
	 * Creates a Card with the given value and suit.
	 * 
	 * @param value the card's value, ranging from 1 to 11.
	 * @param suit the card's suit, ranging from 0 to 2, representing the 3 suits.
	 */
	public Card(int value, int suit) {
		this.value = value;
		this.suit = suit;
	}
	
	/**
	 * Returns the card's value.
	 * 
	 * @return the card's value.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Returns the card's suit.
	 * 
	 * @return the card's suit.
	 */
	public int getSuit() {
		return this.suit;
	}
	
	/**
	 * Returns a negative int if this card comes before the other card,
	 * a positive int if this card comes after,
	 * and 0 if the cards are equal.
	 * Cards are sorted by low suit to high suit,
	 * then low value to high value.
	 * 
	 * @param other the other card being compared to this card.
	 * @return an integer showing this card's relative order to the other card
	 */
	public int compareTo(Card other) {
		if (this.suit == other.suit)
			return this.value - other.value;
		else 
			return this.suit - other.suit;
	}
	
	/**
	 * Returns a string representation of the card.
	 * Formated as "(value) of (suit)"
	 */
	public String toString() {
		return this.value + " of " + SUITS[this.suit];
	}
	
	
}
