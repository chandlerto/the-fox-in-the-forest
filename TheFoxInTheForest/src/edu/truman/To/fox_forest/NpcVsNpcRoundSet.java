package edu.truman.To.fox_forest;

/**
 * Plays a set amount of rounds between 2 Npcs.
 * The player leading each round is randomly selected,
 * so the result of each round is independent to the others. 
 * 
 * @author Chandler To
 *
 */

public class NpcVsNpcRoundSet extends Game {

	static final int HAND_SIZE = 13;
	static final int[] endOfRoundScore = {6,6,6,6,1,2,3,6,6,6,0,0,0,0};
	
	final int NUM_ROUNDS;
	final Deck deck;
	final Npc npc;
	final Npc otherNpc;
	
	int[] roundResults = new int[HAND_SIZE+1];
	Card npcCard;
	Card otherNpcCard;
	Card decreeCard;
	int npcRoundScore;
	int otherNpcRoundScore;
	int npcVictoryPoints;
	int otherNpcVictoryPoints;
	boolean npcFirst;
	boolean isNpcSwan;
	boolean isOtherNpcSwan;
	
	/**
	 * Creates a NpcVsNpcRoundSet with two Npc players.
	 */
	public NpcVsNpcRoundSet(Npc npc, Npc otherNpc, int numRounds) {
		deck = new Deck();
		this.npc = npc;
		this.otherNpc = otherNpc;
		NUM_ROUNDS = numRounds;
	}
	
	/**
	 * Plays a game of The Fox In The Forest.
	 */
	public void playRounds() {
		for (int i = 0; i < NUM_ROUNDS; i++) {
			int coinFlip = (int) (Math.random() * 2);
			npcFirst = coinFlip == 0? true : false;
			deck.shuffle();
			npc.drawHand(deck, HAND_SIZE);
			otherNpc.drawHand(deck, HAND_SIZE);
			decreeCard = deck.draw();
			while (npcRoundScore + otherNpcRoundScore < HAND_SIZE) {
				if (npcFirst) {
					npcCard = npc.selectCardFirst();
					handleFirstThreeAbilities(npc, npcCard);
					otherNpcCard = otherNpc.selectCardSecond(npcCard);
					handleFirstThreeAbilities(otherNpc, otherNpcCard);
				}
				
				else {
					otherNpcCard = otherNpc.selectCardFirst();
					handleFirstThreeAbilities(otherNpc, otherNpcCard);
					npcCard = npc.selectCardSecond(otherNpcCard);
					handleFirstThreeAbilities(npc, npcCard);
				}
				scoreTrick();
				resetTrick();
			}
			scoreRound();
			discardDecreeCard();
		}	
		printResults();
	}
	
	/**
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a Npc plays a card.
	 * 
	 * @param npc the Npc who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Npc npc, Card card) {
		if (card.getValue() == Card.SWAN) {
			if (this.npc == npc) {
				isNpcSwan = true;
			}
			else {
				isOtherNpcSwan = true;
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
		/*
		if (npcCard.getValue() == 7)
			treasureBonus++;
		if (otherNpcCard.getValue() == 7)
			treasureBonus++;
		*/
		
		if (trickWinner() == npc) {
			npcRoundScore++;
			npcVictoryPoints += treasureBonus;
			if (isOtherNpcSwan) {
				npcFirst = false;
			}
			else {
				npcFirst = true;
			}
		}
		else {
			otherNpcRoundScore++;
			otherNpcVictoryPoints += treasureBonus;
			if (isNpcSwan) {
				npcFirst = true;
			}
			else {
				npcFirst = false;
			}
		}
		isNpcSwan = false;
		isOtherNpcSwan = false;
	}
	
	/**
	 * Determines the trick winner and prints the appropriate message.
	 * 
	 * @return the Player that won the trick.
	 */
	private Player trickWinner() {
		boolean isNpcWitch = npcCard.getValue() == Card.WITCH ? true : false;
		boolean isOtherNpcWitch = otherNpcCard.getValue() == Card.WITCH ? true : false;
		if (isNpcWitch && isOtherNpcWitch) {
			if (npcCard.getSuit() == decreeCard.getSuit()) {
				return npc;
			}
			else if (otherNpcCard.getSuit() == decreeCard.getSuit()) {
				return otherNpc;
			}
			else {
				return npcFirst ? npc : otherNpc;
			}
		}
		boolean isNpcTrump = npcCard.getSuit() == decreeCard.getSuit() 
								|| isNpcWitch? true : false;
		boolean isOtherNpcTrump = otherNpcCard.getSuit() == decreeCard.getSuit() 
				|| isOtherNpcWitch? true : false;
		
		if (isNpcTrump && !isOtherNpcTrump) {
			return npc;
		}
		else if (isOtherNpcTrump && !isNpcTrump) {
			return otherNpc;
		}
		else {
			if (npcCard.getValue() > otherNpcCard.getValue()) {
				return npc;
			}
			else if (npcCard.getValue() < otherNpcCard.getValue()) {
				return otherNpc;
			}
			else {
				return npcFirst ? npc : otherNpc;
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
		
		Card temp1 = otherNpcCard;
		otherNpcCard = null;
		deck.putBottom(temp1);
	}
	
	/**
	 * Scores the round, increasing each player's Victory Points
	 * based off their round scores and printing the results.
	 */
	private void scoreRound() {
		roundResults[npcRoundScore]++;
		npcVictoryPoints += endOfRoundScore[npcRoundScore];
		otherNpcVictoryPoints += endOfRoundScore[otherNpcRoundScore];
		npcRoundScore = 0;
		otherNpcRoundScore = 0;
	}
	
	/**
	 * Places the decree card on the bottom of the deck.
	 */
	private void discardDecreeCard() {
		Card temp = decreeCard;
		decreeCard = null;
		deck.putBottom(temp);
	}
	
	private void printResults() {
		
		int wonRounds = 0;
		for (int i = 0; i <= 3; i++) {
			wonRounds += roundResults[i];
		}
		for (int i = 7; i <= 9; i++) {
			wonRounds += roundResults[i];
		}
		
		System.out.println("ROUND RESULTS:");
		for (int i = 0; i <= HAND_SIZE; i++) {
			System.out.println(i + "-" + (HAND_SIZE-i) + ") " + roundResults[i]);
		}
		System.out.println("\nTOTAL VICTORY POINTS (NOT including Treasure bonuses)");
		System.out.println(npcVictoryPoints + "-" + otherNpcVictoryPoints);
		
		double averageNpcVictoryPoints = (double) npcVictoryPoints / NUM_ROUNDS;
		double averageOtherNpcVictoryPoints = (double) otherNpcVictoryPoints / NUM_ROUNDS;
		System.out.println("Average Victory Points per round");
		System.out.println(averageNpcVictoryPoints + "-" + averageOtherNpcVictoryPoints);
		
		System.out.println("WON " + wonRounds + "/" + NUM_ROUNDS);
		System.out.println("WIN PERCENTAGE: " + (double) wonRounds / NUM_ROUNDS * 100);
		
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
		if (player == npc)
			return npcRoundScore;
		else
			return otherNpcRoundScore;
	}
	
	/**
	 * Returns the max integer value, as there are no victory points in this class.
	 * @param a Player in this game
	 * @return Integer.MAX_VALUE
	 */
	public int getPlayerNeededPoints(Player player) {
		return Integer.MAX_VALUE;
	}
}
