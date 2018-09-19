package edu.truman.To.fox_forest;

/**
 * Represents the entire game, including the players, cards, and scores.
 * Both Players are controlled by the computer.
 * 
 * @author Chandler To
 *
 */

public class NpcVsNpcGame extends Game {

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
	 * Creates a Game with two NPC players.
	 */
	public NpcVsNpcGame(Npc npc, Npc otherNpc) {
		deck = new Deck();
		p1 = npc;
		p2 = otherNpc;
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
			while (p1RoundScore + p2RoundScore < HAND_SIZE) {
				if (p1First) {
					p1Card = p1.selectCardFirst();
					handleFirstThreeAbilities((Npc) p1, p1Card);
					p2Card = p2.selectCardSecond(p1Card);
					handleFirstThreeAbilities((Npc) p2, p2Card);
				}
				
				else {
					p2Card = p2.selectCardFirst();
					handleFirstThreeAbilities((Npc) p2, p2Card);
					p1Card = p1.selectCardSecond(p2Card);
					handleFirstThreeAbilities((Npc) p1, p1Card);
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
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a NPC plays a card.
	 * 
	 * @param npc the NPC who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Npc npc, Card card) {
		if (card.getValue() == Card.SWAN) {
			if (p1 == npc) {
				isP1Swan = true;
			}
			else {
				isP2Swan = true;
			}
		}
		else if (card.getValue() == Card.FOX) {
			decreeCard = npc.swapDecreeCard(decreeCard);
		}
		else if (card.getValue() == Card.WOODCUTTER) {
			deck.putBottom(npc.drawAndDiscard(deck.draw()));
		}
	}
	
	/**
	 * Scores the trick, awarding the appropriate Victory Points (if any),
	 * awarding the winner a round point, 
	 * and determines which player will lead the next trick.
	 */
	private void scoreTrick() {
		int treasureBonus = 0;
		if (p1Card.getValue() == 7)
			treasureBonus++;
		if (p2Card.getValue() == 7)
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
		boolean isNpcWitch = p1Card.getValue() == Card.WITCH ? true : false;
		boolean isOtherNpcWitch = p2Card.getValue() == Card.WITCH ? true : false;
		if (isNpcWitch && isOtherNpcWitch) {
			if (p1Card.getSuit() == decreeCard.getSuit()) {
				return p1;
			}
			else if (p2Card.getSuit() == decreeCard.getSuit()) {
				return p2;
			}
			else {
				return p1First ? p1 : p2;
			}
		}
		boolean isNpcTrump = p1Card.getSuit() == decreeCard.getSuit() 
								|| isNpcWitch? true : false;
		boolean isOtherNpcTrump = p2Card.getSuit() == decreeCard.getSuit() 
				|| isOtherNpcWitch? true : false;
		
		if (isNpcTrump && !isOtherNpcTrump) {
			return p1;
		}
		else if (isOtherNpcTrump && !isNpcTrump) {
			return p2;
		}
		else {
			if (p1Card.getValue() > p2Card.getValue()) {
				return p1;
			}
			else if (p1Card.getValue() < p2Card.getValue()) {
				return p2;
			}
			else {
				return p1First ? p1 : p2;
			}
		}	
	}
	
	/**
	 * Places the Player's cards onto the bottom of the deck.
	 */
	private void resetTrick() {
		Card temp = p1Card;
		p1Card = null;
		deck.putBottom(temp);
		
		Card temp1 = p2Card;
		p2Card = null;
		deck.putBottom(temp1);
	}
	
	/**
	 * Scores the round, increasing each player's Victory Points
	 * based off their round scores and printing the results.
	 */
	private void scoreRound() {
		System.out.println("\n\nEND OF ROUND RESULTS: ");
		System.out.println(p1RoundScore + "-" + p2RoundScore);
		System.out.println("P1 gains +" + endOfRoundScore[p1RoundScore]);
		System.out.println("P2 gains +" + endOfRoundScore[p2RoundScore]);
		p1VictoryPoints += endOfRoundScore[p1RoundScore];
		p2VictoryPoints += endOfRoundScore[p2RoundScore];
		System.out.println("CURRENT VICTORY POINTS: ");
		System.out.println(p1VictoryPoints + "-" + p2VictoryPoints);
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
			System.out.println("P1 WINS");
		}
		else if (p1VictoryPoints < p2VictoryPoints) {
			System.out.println("P2 WINS");
		}
		else if (endOfRoundScore[p1RoundScore] > endOfRoundScore[p2RoundScore]) {
			System.out.println("P1 WINS BY WINNING THE LAST ROUND");
		}
		else {
			System.out.println("P2 WINS BY WINNING THE LAST ROUND");
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
		if (player == p1)
			return p2RoundScore;
		else
			return p1RoundScore;
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
