package edu.truman.To.fox_forest;

/**
 * Represents a character playing the game, whether
 * controlled by the user or by the program.
 * 
 * @author Chandler To
 *
 */


public interface Player {
	
	/**
	 * Adds cards to the player's hand from the deck to reach the desired size.
	 * After cards are drawn, the cards are then sorted.
	 * @param deck the deck the player is drawing from.
	 * @param handSize the amount of cards the player will have in hand.
	 * @precondition handSize + the player's current hand size <= deck's size
	 */
	void drawHand(Deck deck, int handSize);
	
	/**
	 * Sorts the player's hand by suit, then by value.
	 */
	void sortHand();
	
	/**
	 * Select (and remove) a card from the player's hand.
	 * Used when the player is leading the trick.
	 * 
	 * @return the card the player is using.
	 */
	Card selectCardFirst();
	
	/**
	 * Select (and remove) a card from the player's hand.
	 * Used when the player is following the trick.
	 * 
	 * @param lead the card that is leading the trick. The player's card
	 * 			   must match suit of this card, if possible.
	 * @return the card the player is using.
	 */
	Card selectCardSecond(Card lead);
	
	/**
	 * Adds a card to the player's hand, then removes a card of the player's choice.
	 * Used when the player has played a card with the "Fox" ability, which allows
	 * the player to switch the decree card with a card from hand.
	 * 
	 * @param current the current decree card.
	 * @return the card that the player wishes to discard.
	 */
	Card swapDecreeCard(Card current);
	
	/**
	 * Adds a card to the player's hand, then removes a card of the player's choice.
	 * Used when the player has played a card with the "Woodcutter" ability, which makes
	 * the player draw a card from the deck, then discard a card from hand.
	 * 
	 * @param drawn the card which was drawn from the deck.
	 * @return the card that the player wishes to discard.
	 */
	Card drawAndDiscard(Card drawn);
	
}
