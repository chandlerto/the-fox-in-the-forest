package edu.truman.To.fox_forest;

/**
 * Represents the entire game, including the players, cards, and scores.
 * Both Players are controlled by the user.
 * 
 * @author Chandler To
 *
 */

public class TwoHumanGame extends Game {

	static final int HAND_SIZE = 13;
	static final int WIN_SCORE = 21;
	static final int[] endOfRoundScore = {6,6,6,6,1,2,3,6,6,6,0,0,0,0};
	
	final Deck deck;
	Card p1Card;
	Card p2Card;
	Card decreeCard;
	int p1RoundScore;
	int p2RoundScore;
	int p1VictoryPoints;
	int p2VictoryPoints;
	boolean p1First;
	boolean isP1Swan;
	boolean isP2Swan;
	
	/**
	 * Creates a Game with two Human players.
	 */
	public TwoHumanGame() {
		deck = new Deck();
		p1 = new Human();
		p2 = new Human();
	}
	
	/**
	 * Plays a game of The Fox In The Forest.
	 */
	public void playGame() {
		int coinFlip = (int) (Math.random() * 2);
		p1First = coinFlip == 0? true : false;
		while (p1VictoryPoints < WIN_SCORE && p2VictoryPoints < WIN_SCORE) {
			deck.shuffle();
			p1.drawHand(deck, HAND_SIZE);
			p2.drawHand(deck, HAND_SIZE);
			decreeCard = deck.draw();
			while (p1RoundScore + p2RoundScore < 13) {
				if (p1First) {
					printGameInfo();
					System.out.println("HUMAN IS LEADING");
					p1Card = p1.selectCardFirst();
					handleFirstThreeAbilities(p1, p1Card);
					
					printGameInfo();
					System.out.println("NPC IS FOLLOWING");
					p2Card = p2.selectCardSecond(p1Card);
					handleFirstThreeAbilities(p2, p2Card);
				}
				
				else {
					printGameInfo();
					System.out.println("NPC IS LEADING");
					p2Card = p2.selectCardFirst();
					handleFirstThreeAbilities(p2, p2Card);
					
					printGameInfo();
					System.out.println("HUMAN IS FOLLOWING");
					p1Card = p1.selectCardSecond(p2Card);
					handleFirstThreeAbilities(p1, p1Card);
				}
				scoreTrick();
				resetTrick();
			}
			scoreRound();
			discardDecreeCard();
		}	
		determineWinnerAndPrint();
	}
	
	/**
	 * Prints to the console the state of the game.
	 * This includes each player's Victory Points, current Round Score,
	 * the decree card, and each player's current card.
	 */
	private void printGameInfo() {
		System.out.println("\nVP: " + p1VictoryPoints + "-" + p2VictoryPoints);
		System.out.println("RS: " + p1RoundScore + "-" + p2RoundScore);
		System.out.println("Decree Card: " + decreeCard.toString());
		if (p1Card != null) {
			System.out.println("Human Card: " + p1Card.toString());
		}
		else {
			System.out.println("Human Card: Not yet selected");
		}
		if (p2Card != null) {
			System.out.println("NPC Card: " + p2Card.toString());
		}
		else {
			System.out.println("NPC Card: Not yet selected");
		}
		System.out.println();
	}
	
	/**
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a player plays a card.
	 * 
	 * @param player the player who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Player player, Card card) {
		if (card.getValue() == Card.SWAN) {
			if (player == p1) {
				isP1Swan = true;
			}
			else if (player == p2) {
				isP2Swan = true;
			}
		}
		if (card.getValue() == Card.FOX) {
			decreeCard = player.swapDecreeCard(decreeCard);
		}
		else if (card.getValue() == Card.WOODCUTTER) {
			deck.putBottom(player.drawAndDiscard(deck.draw()));
		}
	}
	
	/**
	 * Scores the trick, awarding the appropriate Victory Points (if any),
	 * awarding the winner a round point, 
	 * and determines which player will lead the next trick.
	 */
	private void scoreTrick() {
		int treasureBonus = 0;
		if (p1Card.getValue() == Card.TREASURE)
			treasureBonus++;
		if (p2Card.getValue() == Card.TREASURE)
			treasureBonus++;
		
		if (trickWinner() == p1) {
			p1RoundScore++;
			p1VictoryPoints += treasureBonus;
			if (isP2Swan) {
				p1First = false;
			}
			else {
				p1First = true;
			}
		}
		else {
			p2RoundScore++;
			p2VictoryPoints += treasureBonus;
			if (isP1Swan) {
				p1First = true;
			}
			else {
				p1First = false;
			}
		}
		isP1Swan = false;
		isP2Swan = false;
	}
	
	/**
	 * Determines the trick winner and prints the appropriate message.
	 * 
	 * @return the Player that won the trick.
	 */
	private Player trickWinner() {
		boolean isP1Witch = p1Card.getValue() == Card.WITCH ? true : false;
		boolean isP2Witch = p2Card.getValue() == Card.WITCH ? true : false;
		if (isP1Witch && isP2Witch) {
			if (p1Card.getSuit() == decreeCard.getSuit()) {
				System.out.println("Human wins by trump suit");
				return p1;
			}
			else if (p2Card.getSuit() == decreeCard.getSuit()) {
				System.out.println("NPC wins by trump suit");
				return p2;
			}
			else {
				System.out.println("Tie, so leading player wins the trick");
				return p1First ? p1 : p2;
			}
		}
		boolean isP1Trump = p1Card.getSuit() == decreeCard.getSuit() 
								|| isP1Witch? true : false;
		boolean isP2Trump = p2Card.getSuit() == decreeCard.getSuit() 
				|| isP2Witch? true : false;
		
		if (isP1Trump && !isP2Trump) {
			System.out.println("Human wins by trump suit");
			return p1;
		}
		else if (isP2Trump && !isP1Trump) {
			System.out.println("NPC wins by trump suit");
			return p2;
		}
		else {
			if (p1Card.getValue() > p2Card.getValue()) {
				System.out.println("Human wins by higher value");
				return p1;
			}
			else if (p1Card.getValue() < p2Card.getValue()) {
				System.out.println("NPC wins by higher value");
				return p2;
			}
			else {
				System.out.println("Tie, so leading player wins the trick");
				return p1First ? p1 : p2;
			}
		}
		
	}
	
	/**
	 * Places the Player's cards onto the bottom of the deck.
	 */
	
	private void resetTrick() {
		Card temp = p2Card;
		p2Card = null;
		deck.putBottom(temp);
		
		Card temp1 = p1Card;
		p1Card = null;
		deck.putBottom(temp1);
	}
	
	/**
	 * Scores the round, increasing each player's Victory Points
	 * based off their round scores and printing the results.
	 */
	private void scoreRound() {
		System.out.println("\n\nEND OF ROUND RESULTS: ");
		System.out.println(p1RoundScore + "-" + p2RoundScore);
		System.out.println("Human gains +" + endOfRoundScore[p1RoundScore]);
		System.out.println("NPC gains +" + endOfRoundScore[p2RoundScore]);
		System.out.println("\n");
		p1VictoryPoints += endOfRoundScore[p1RoundScore];
		p2VictoryPoints += endOfRoundScore[p2RoundScore];
		p1RoundScore = 0;
		p2RoundScore = 0;
	}
	
	/**
	 * Places the decree card on the bottom of the deck.
	 */
	private void discardDecreeCard() {
		Card temp = decreeCard;
		decreeCard = null;
		deck.putBottom(temp);
	}
	
	/**
	 * Determines the winner of the game and prints the results.
	 */
	private void determineWinnerAndPrint() {
		System.out.println("\n\nFINAL SCORE: " + p1VictoryPoints + "-" + p2VictoryPoints);
		if (p1VictoryPoints > p2VictoryPoints) {
			System.out.println("You won!");
		}
		else if (p1VictoryPoints < p2VictoryPoints) {
			System.out.println("You lost...");
		}
		else if (endOfRoundScore[p1RoundScore] > endOfRoundScore[p2RoundScore]) {
			System.out.println("You won the last round, so you win!");
		}
		else {
			System.out.println("You lost the last round, so you lose...");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Card getDecreeCard() {
		return this.decreeCard;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPlayerRoundScore(Player player) {
		if (player == p1)
			return p1RoundScore;
		else
			return p2RoundScore;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getOtherRoundScore(Player player) {
		if (player == p2)
			return p1RoundScore;
		else
			return p2RoundScore;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPlayerNeededPoints(Player player) {
		if (player == p1)
			return WIN_SCORE - this.p1VictoryPoints;
		else
			return WIN_SCORE - this.p2VictoryPoints;
	}
	
}
