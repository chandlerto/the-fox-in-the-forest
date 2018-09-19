package edu.truman.To.fox_forest;

import java.util.ArrayList;

/**
 * An extremely simple NPC (Player controlled by the computer).
 * Always plays the lowest valued eligible card in hand
 * (as determined by the Card compareTo method),
 * and doesn't use the Fox or Woodcutter abilities by always
 * discarding the card that was just gained.
 * 
 * @author Chandler To
 *
 */

public class LowDummyNpc extends Npc {

	private ArrayList<Card> hand;
	
	/**
	 * Creates a LowDummyNpc with the given Game.
	 */
	public LowDummyNpc() {
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
	 * Selects and removes the lowest valued card in hand,
	 * as determined by the Card class' compareTo method.
	 * 
	 * @return the lowest valued card in hand
	 */
	@Override
	public Card selectCardFirst() {
		return hand.remove(0);
	}

	/**
	 * Selects and removes the lowest valued card matching suit,
	 * or the lowest valued card if none match suit.
	 * 
	 * @param lead the card that is leading the trick. The player's card
	 * 			   must match suit of this card, if possible.
	 * @return the card the player is using.
	 */
	@Override
	public Card selectCardSecond(Card lead) {
		int leadSuit = lead.getSuit();
		
		if (lead.getValue() == Card.MONARCH) {
			boolean hasSuit = false;
			int highIdx = 0;
			for (int i = hand.size()-1 ; i >= 0; i--) {
				if (hand.get(i).getSuit() == leadSuit && !hasSuit) {
					highIdx = i;
					hasSuit = true;
				}
				else if (hand.get(i).getSuit() == leadSuit &&
						 hand.get(i).getValue() == 1) {
					return hand.remove(i);
				}
			}
			return hand.remove(highIdx);
		}
	
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getSuit() == leadSuit) {
				return hand.remove(i);
			}
		}
		return hand.remove(0);
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
