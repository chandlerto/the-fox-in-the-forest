package edu.truman.To.fox_forest;

/**
 * Represents the entire game, including the players, cards, and scores.
 * One player is controlled by the user, while the other
 * is controlled by the computer.
 * 
 * @author Chandler To
 *
 */
public class HumanVsNpcGame extends Game {

	static final int HAND_SIZE = 13;
	static final int WIN_SCORE = 21;
	static final int[] endOfRoundScore = {6,6,6,6,1,2,3,6,6,6,0,0,0,0};
	
	final Deck deck;
	final Human human;
	final Npc npc;
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
	 * Creates a Game with a human player and an npc player.
	 */
	public HumanVsNpcGame(Npc npc) {
		deck = new Deck();
		human = new Human();
		this.npc = npc;
	}
	
	/**
	 * Plays a game of The Fox In The Forest.
	 */
	public void playGame() {
		int coinFlip = (int) (Math.random() * 2);
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
					
					npcCard = npc.selectCardSecond(humanCard);
					printNpcTurn();
					handleFirstThreeAbilities(npc, npcCard);
					
				}
				
				else {
					
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
		System.out.println("\nVP: " + humanVictoryPoints + "-" + npcVictoryPoints);
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
		System.out.println();
	}
	
	/**
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a Human plays a card.
	 * 
	 * @param human the Human who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Human human, Card card) {
		if (card.getValue() == Card.SWAN) {
			isHumanSwan = true;
		}
		if (card.getValue() == Card.FOX) {
			decreeCard = human.swapDecreeCard(decreeCard);
		}
		else if (card.getValue() == Card.WOODCUTTER) {
			deck.putBottom(human.drawAndDiscard(deck.draw()));
		}
	}
	
	/**
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a Human plays a card.
	 * 
	 * @param human the Human who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Npc npc, Card card) {
		if (card.getValue() == Card.SWAN) {
			isNpcSwan = true;
		}
		if (card.getValue() == Card.FOX) {
			System.out.println("\nNpc triggers the Fox ability.");
			System.out.println("Npc places the " + decreeCard.toString() + " into its hand.");
			decreeCard = npc.swapDecreeCard(decreeCard);
			System.out.println("The new decree card is the " + decreeCard.toString() + ".");
		}
		else if (card.getValue() == Card.WOODCUTTER) {
			System.out.println("\nNpc triggers the Woodcutter ability.");
			System.out.println("Npc draws a card from the deck, then discards a card.");
			deck.putBottom(npc.drawAndDiscard(deck.draw()));
		}
	}
	
	/**
	 * Prints a message showing the card the npc just played.
	 * Used when the npc plays a card.
	 */
	private void printNpcTurn() {
		if (humanFirst) {
			System.out.println("Npc follows with the " + npcCard.toString());
		}
		else {
			System.out.println("Npc leads with the " + npcCard.toString()); 
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
		boolean isHumanWitch = humanCard.getValue() == Card.WITCH ? true : false;
		boolean isNpcWitch = npcCard.getValue() == Card.WITCH ? true : false;
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
		System.out.println("\n\nEND OF ROUND RESULTS: ");
		System.out.println(humanRoundScore + "-" + npcRoundScore);
		System.out.println("Human gains +" + endOfRoundScore[humanRoundScore]);
		System.out.println("NPC gains +" + endOfRoundScore[npcRoundScore]);
		System.out.println("\n");
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
		System.out.println("\n\nFINAL SCORE: " + humanVictoryPoints + "-" + npcVictoryPoints);
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
		if (player == human)
			return humanRoundScore;
		else
			return npcRoundScore;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPlayerNeededPoints(Player player) {
		if (player == human)
			return WIN_SCORE - this.humanVictoryPoints;
		else
			return WIN_SCORE - this.npcVictoryPoints;
	}
}
