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
	 * Sets this player's game to be the given Game.
	 * @param game the game the Npc is a part of
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

}
