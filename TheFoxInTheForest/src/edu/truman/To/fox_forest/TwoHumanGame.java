package edu.truman.To.fox_forest;

/**
 * Represents the entire game, including the players, cards, and scores.
 * 
 * @author ct2883
 *
 */

public class TwoHumanGame {

	static final int HAND_SIZE = 13;
	static final int WIN_SCORE = 21;
	static final int[] endOfRoundScore = {6,6,6,6,1,2,3,6,6,6,0,0,0,0};
	
	final Deck deck;
	final Human human;
	final Human npc;
	Card humanCard;
	Card npcCard;
	Card decreeCard;
	int humanRoundScore;
	int npcRoundScore;
	int humanVictoryPoints;
	int npcVictoryPoints;
	boolean humanFirst;
	boolean isHumanSwan;
	boolean isNpcSwan;
	
	/**
	 * Creates a Game with two Human players.
	 */
	public TwoHumanGame() {
		deck = new Deck();
		human = new Human();
		npc = new Human();
	}
	
	/**
	 * Plays a game of The Fox In The Forest.
	 */
	public void playGame() {
		int coinFlip = (int) (Math.random());
		humanFirst = coinFlip == 0? true : false;
		while (humanVictoryPoints < WIN_SCORE && npcVictoryPoints < WIN_SCORE) {
			deck.shuffle();
			human.drawHand(deck, HAND_SIZE);
			npc.drawHand(deck, HAND_SIZE);
			decreeCard = deck.draw();
			while (humanRoundScore + npcRoundScore < 13) {
				if (humanFirst) {
					printGameInfo();
					System.out.println("HUMAN IS LEADING");
					humanCard = human.selectCardFirst();
					handleFirstThreeAbilities(human, humanCard);
					
					printGameInfo();
					System.out.println("NPC IS FOLLOWING");
					npcCard = npc.selectCardSecond(humanCard);
					handleFirstThreeAbilities(npc, npcCard);
				}
				
				else {
					printGameInfo();
					System.out.println("NPC IS LEADING");
					npcCard = npc.selectCardFirst();
					handleFirstThreeAbilities(npc, npcCard);
					
					printGameInfo();
					System.out.println("HUMAN IS FOLLOWING");
					humanCard = human.selectCardSecond(npcCard);
					handleFirstThreeAbilities(human, humanCard);
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
		System.out.println("VP: " + humanVictoryPoints + "-" + npcVictoryPoints);
		System.out.println("RS: " + humanRoundScore + "-" + npcRoundScore);
		System.out.println("Decree Card: " + decreeCard.getValue() + " of " + Card.SUITS[decreeCard.getSuit()]);
		if (humanCard != null) {
			System.out.println("Human Card: " + humanCard.getValue() + " of " + Card.SUITS[humanCard.getSuit()]);
		}
		else {
			System.out.println("Human Card: Not yet selected");
		}
		if (npcCard != null) {
			System.out.println("NPC Card: " + npcCard.getValue() + " of " + Card.SUITS[npcCard.getSuit()]);
		}
		else {
			System.out.println("NPC Card: Not yet selected");
		}
	}
	
	/**
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a player plays a card.
	 * 
	 * @param player the player who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Player player, Card card) {
		if (card.getValue() == Card.SWAN_VALUE) {
			if (player == human) {
				isHumanSwan = true;
			}
			else if (player == npc) {
				isNpcSwan = true;
			}
		}
		if (card.getValue() == Card.FOX_VALUE) {
			decreeCard = player.swapDecreeCard(decreeCard);
		}
		else if (card.getValue() == Card.WOODCUTTER_VALUE) {
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
		if (humanCard.getValue() == 7)
			treasureBonus++;
		if (npcCard.getValue() == 7)
			treasureBonus++;
		
		if (trickWinner() == human) {
			humanRoundScore++;
			humanVictoryPoints += treasureBonus;
			if (isNpcSwan) {
				humanFirst = false;
				
			}
			else {
				humanFirst = true;
			}
		}
		else {
			npcRoundScore++;
			npcVictoryPoints += treasureBonus;
			if (isHumanSwan) {
				humanFirst = true;
				
			}
			else {
				humanFirst = false;
			}
		}
		isHumanSwan = false;
		isNpcSwan = false;
	}
	
	/**
	 * Determines the trick winner and prints the appropriate message.
	 * 
	 * @return the Player that won the trick.
	 */
	private Player trickWinner() {
		boolean isHumanWitch = humanCard.getValue() == Card.WITCH_VALUE ? true : false;
		boolean isNpcWitch = npcCard.getValue() == Card.WITCH_VALUE ? true : false;
		if (isHumanWitch && isNpcWitch) {
			if (humanCard.getSuit() == decreeCard.getSuit()) {
				System.out.println("Human wins by trump suit");
				return human;
			}
			else if (npcCard.getSuit() == decreeCard.getSuit()) {
				System.out.println("NPC wins by trump suit");
				return npc;
			}
			else {
				System.out.println("Tie, so leading player wins the trick");
				return humanFirst ? human : npc;
			}
		}
		boolean isHumanTrump = humanCard.getSuit() == decreeCard.getSuit() 
								|| isHumanWitch? true : false;
		boolean isNpcTrump = npcCard.getSuit() == decreeCard.getSuit() 
				|| isNpcWitch? true : false;
		
		if (isHumanTrump && !isNpcTrump) {
			System.out.println("Human wins by trump suit");
			return human;
		}
		else if (isNpcTrump && !isHumanTrump) {
			System.out.println("NPC wins by trump suit");
			return npc;
		}
		else {
			if (humanCard.getValue() > npcCard.getValue()) {
				System.out.println("Human wins by higher value");
				return human;
			}
			else if (humanCard.getValue() < npcCard.getValue()) {
				System.out.println("NPC wins by higher value");
				return npc;
			}
			else {
				System.out.println("Tie, so leading player wins the trick");
				return humanFirst ? human : npc;
			}
		}
		
	}
	
	/**
	 * Places the Player's cards onto the bottom of the deck.
	 */
	
	private void resetTrick() {
		Card temp = npcCard;
		npcCard = null;
		deck.putBottom(temp);
		
		Card temp1 = humanCard;
		humanCard = null;
		deck.putBottom(temp1);
	}
	
	/**
	 * Scores the round, increasing each player's Victory Points
	 * based off their round scores and printing the results.
	 */
	private void scoreRound() {
		System.out.println("END OF ROUND RESULTS: ");
		System.out.println(humanRoundScore + "-" + npcRoundScore);
		System.out.println("Human gains +" + endOfRoundScore[humanRoundScore]);
		System.out.println("NPC gains +" + endOfRoundScore[npcRoundScore]);
		humanVictoryPoints += endOfRoundScore[humanRoundScore];
		npcVictoryPoints += endOfRoundScore[npcRoundScore];
		humanRoundScore = 0;
		npcRoundScore = 0;
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
		System.out.println("FINAL SCORE: " + humanVictoryPoints + "-" + npcVictoryPoints);
		if (humanVictoryPoints > npcVictoryPoints) {
			System.out.println("You won!");
		}
		else if (humanVictoryPoints < npcVictoryPoints) {
			System.out.println("You lost...");
		}
		else if (endOfRoundScore[humanRoundScore] > endOfRoundScore[npcRoundScore]) {
			System.out.println("You won the last round, so you win!");
		}
		else {
			System.out.println("You lost the last round, so you lose...");
		}
	}
	
	
}
