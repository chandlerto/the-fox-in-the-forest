package edu.truman.To.fox_forest;

import java.util.ArrayList;

/**
 * An extremely simple NPC (Player controlled by the computer).
 * Always plays the highest valued eligible card in hand
 * (as determined by the Card compareTo method),
 * and doesn't use the Fox or Woodcutter abilities by always
 * discarding the card that was just gained.
 * 
 * @author Chandler To
 *
 */

public class HighDummyNpc extends Npc {

	private ArrayList<Card> hand;
	
	/**
	 * Creates a HighDummyNpc with no cards.
	 */
	public HighDummyNpc() {
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
	 * Selects and removes the highest valued card in hand,
	 * as determined by the Card class' compareTo method.
	 * 
	 * @return the highest valued card in hand
	 */
	@Override
	public Card selectCardFirst() {
		return hand.remove(hand.size()-1);
	}

	/**
	 * Selects and removes the highest valued card matching suit,
	 * or the highest valued card if none match suit.
	 * 
	 * @param lead the card that is leading the trick. The player's card
	 * 			   must match suit of this card, if possible.
	 * @return the card the player is using.
	 */
	@Override
	public Card selectCardSecond(Card lead) {
		int leadSuit = lead.getSuit();
	
		for (int i = hand.size()-1; i >= 0; i--) {
			if (hand.get(i).getSuit() == leadSuit) {
				return hand.remove(i);
			}
		}
		return hand.remove(hand.size()-1);
	}

	/**
	 * Computer is offered a card, but discards it immediately.
	 * Used when the player has played a card with the "Fox" ability, which allows
	 * the player to switch the decree card with a card from hand.
	 * 
	 * @param current the card that is offered, but discarded.
	 * @return the card that is discarded (in this case, the given card)
	 */
	@Override
	public Card swapDecreeCard(Card current) {
		return current;
	}

	
	/**
	 * Computer is offered a card, but discards it immediately.
	 * Used when the player has played a card with the "Woodcutter" ability, which makes
	 * the player draw a card from the deck, then discard a card from hand.
	 * 
	 * @param drawn the card that is offered, but discarded.
	 * @return the card that is discarded (in this case, the given card)
	 */
	@Override
	public Card drawAndDiscard(Card drawn) {
		return drawn;
	}

}

