package edu.truman.To.fox_forest;

/**
 * A "game" of Fox in the Forest. This could be a single game to a 
 * certain victory point score, or a set amount of rounds.
 * 
 * @author ct2883
 *
 */
public abstract class Game {

	final Deck deck;
	Player p1;
	Player p2;
	
	/**
	 * Default constructor for a Game.
	 * Shouldn't actually be used.
	 */
	public Game() {
		deck = new Deck();
		p1 = new Human();
		p2 = new Human();
	}
	
	/**
	 * Return the decree card.
	 * @return the decree card.
	 */
	public abstract Card getDecreeCard();
	
	/**
	 * Returns the given player's round score.
	 * @param player a Player in this Game
	 * @return the player's round score.
	 */
	public abstract int getPlayerRoundScore(Player player);
	
	/**
	 * Returns the given player's opponent's round score.
	 * @param player a Player in this Game
	 * @return the player's opponent's round score.
	 */
	public abstract int getOtherRoundScore(Player player);
	
	/**
	 * Returns the number of points needed for the given player to win.
	 * @param player a Player in this Game
	 * @return the number of points needed for the given player to win.
	 */
	public abstract int getPlayerNeededPoints(Player player);
	
}
