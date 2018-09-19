package edu.truman.To.fox_forest;

/**
 * 
 * A Player controlled by the computer.
 * 
 * @author Chandler To
 *
 */

public abstract class Npc implements Player {

	protected Game game;
	
	/**
	 * Sets this NPC's game to be the given Game.
	 * @param game the game the NPC is a part of
	 */
	public void addGame(Game game) {
		this.game = game;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void drawHand(Deck deck, int handSize);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void sortHand();
	
	/**
	 * Returns an integer showing the computer's strategy. 
	 * A value over 0 denotes high, while a value of 0 or less denotes low.
	 * For NPCs which only use a single strategy, 0 is always returned.
	 * 
	 * @return the current strategy of the computer
	 */
	public abstract int getStrategy();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Card selectCardFirst();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Card selectCardSecond(Card lead);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Card swapDecreeCard(Card current);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Card drawAndDiscard(Card drawn);
	
	/**
	 * Places all cards in the NPC's hand into the given Deck.
	 * 
	 * @param deck the deck that the NPC discards to
	 */
	public abstract void clearHand(Deck deck);

}
