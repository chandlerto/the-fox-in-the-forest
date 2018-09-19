package edu.truman.To.fox_forest;

/**
 * Plays a set amount of rounds between 2 NPCs.
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
	
	int[] roundResults = new int[HAND_SIZE+1];
	Card p1_Card;
	Card p2_Card;
	Card decreeCard;
	int p1_RoundScore;
	int p2_RoundScore;
	int p1_VictoryPoints;
	int p2_VictoryPoints;
	boolean p1_First;
	boolean isp1_Swan;
	boolean isp2_Swan;
	
	int p1_StrategyRequirement;
	int p2_StrategyRequirement;
	boolean playingRound;
	int recordedRounds;
	
	/**
	 * Creates a NpcVsNpcRoundSet with two NPC players.
	 */
	public NpcVsNpcRoundSet(Npc npc, Npc otherNpc, int numRounds, int npcStrat, int otherNpcStrat) {
		deck = new Deck();
		p1 = npc;
		p2 = otherNpc;
		NUM_ROUNDS = numRounds;
		p1_StrategyRequirement = npcStrat;
		p2_StrategyRequirement = otherNpcStrat;
	}
	
	/**
	 * Plays a game of The Fox In The Forest.
	 */
	public void playRounds() {
		while (recordedRounds < NUM_ROUNDS) {
			int coinFlip = (int) (Math.random() * 2);
			p1_First = coinFlip == 0? true : false;
			
			deck.shuffle();
			p1.drawHand(deck, HAND_SIZE);
			p2.drawHand(deck, HAND_SIZE);
			decreeCard = deck.draw();
			
			decideIfPlayingRound();
			if (playingRound) {
				while (p1_RoundScore + p2_RoundScore < HAND_SIZE) {
					if (p1_First) {
						p1_Card = p1.selectCardFirst();
						handleFirstThreeAbilities((Npc) p1, p1_Card);
						p2_Card = p2.selectCardSecond(p1_Card);
						handleFirstThreeAbilities((Npc) p2, p2_Card);
					}
					
					else {
						p2_Card = p2.selectCardFirst();
						handleFirstThreeAbilities((Npc) p2, p2_Card);
						p1_Card = p1.selectCardSecond(p2_Card);
						handleFirstThreeAbilities((Npc) p1, p1_Card);
					}
					scoreTrick();
					resetTrick();
				}
				scoreAndResetRound();
			}
			else {
				collectAllCards();
			}
		}	
		printResults();
	}
	
	/**
	 * Used to determine if the round meets the preset requirements
	 * and should be played and recorded.
	 */
	private void decideIfPlayingRound() {
		playingRound = true;
		int npcStrat = ((Npc) p1).getStrategy();
		int otherNpcStrat = ((Npc) p2).getStrategy();
		if (p2_StrategyRequirement != 0 || p1_StrategyRequirement != 0) {
			if (p2_StrategyRequirement < 0 && otherNpcStrat > 0 ||
				p2_StrategyRequirement > 0 && otherNpcStrat <= 0 ||
				p1_StrategyRequirement < 0 && npcStrat > 0 ||
				p1_StrategyRequirement > 0 && npcStrat <= 0)
				playingRound = false;
		}
	}
	
	/**
	 * Takes all cards in the game and puts them into the deck.
	 */
	private void collectAllCards() {
		((Npc) p1).clearHand(deck);
		((Npc) p2).clearHand(deck);
		discardDecreeCard();
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
				isp1_Swan = true;
			}
			else {
				isp2_Swan = true;
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
	 * FOR THIS CLASS, TREASURE BONUS IS IGNORED.
	 */
	private void scoreTrick() {
		int treasureBonus = 0;
	
		if (trickWinner() == p1) {
			p1_RoundScore++;
			p1_VictoryPoints += treasureBonus;
			if (isp2_Swan) {
				p1_First = false;
			}
			else {
				p1_First = true;
			}
		}
		else {
			p2_RoundScore++;
			p2_VictoryPoints += treasureBonus;
			if (isp1_Swan) {
				p1_First = true;
			}
			else {
				p1_First = false;
			}
		}
		isp1_Swan = false;
		isp2_Swan = false;
	}
	
	/**
	 * Determines the trick winner and prints the appropriate message.
	 * 
	 * @return the Player that won the trick.
	 */
	private Player trickWinner() {
		boolean isNpcWitch = p1_Card.getValue() == Card.WITCH ? true : false;
		boolean isOtherNpcWitch = p2_Card.getValue() == Card.WITCH ? true : false;
		if (isNpcWitch && isOtherNpcWitch) {
			if (p1_Card.getSuit() == decreeCard.getSuit()) {
				return p1;
			}
			else if (p2_Card.getSuit() == decreeCard.getSuit()) {
				return p2;
			}
			else {
				return p1_First ? p1 : p2;
			}
		}
		boolean isNpcTrump = p1_Card.getSuit() == decreeCard.getSuit() 
								|| isNpcWitch? true : false;
		boolean isOtherNpcTrump = p2_Card.getSuit() == decreeCard.getSuit() 
				|| isOtherNpcWitch? true : false;
		
		if (isNpcTrump && !isOtherNpcTrump) {
			return p1;
		}
		else if (isOtherNpcTrump && !isNpcTrump) {
			return p2;
		}
		else {
			if (p1_Card.getValue() > p2_Card.getValue()) {
				return p1;
			}
			else if (p1_Card.getValue() < p2_Card.getValue()) {
				return p2;
			}
			else {
				return p1_First ? p1 : p2;
			}
		}	
	}
	
	/**
	 * Places the Player's cards onto the bottom of the deck.
	 */
	private void resetTrick() {
		Card temp = p1_Card;
		p1_Card = null;
		deck.putBottom(temp);
		
		Card temp1_ = p2_Card;
		p2_Card = null;
		deck.putBottom(temp1_);
	}
	
	/**
	 * Scores the round, increasing each player's Victory Points
	 * based off their round scores and printing the results.
	 */
	private void scoreAndResetRound() {
		if (playingRound) {
			roundResults[p1_RoundScore]++;
			recordedRounds++;
			p1_VictoryPoints += endOfRoundScore[p1_RoundScore];
			p2_VictoryPoints += endOfRoundScore[p2_RoundScore];
		}
		
		p1_RoundScore = 0;
		p2_RoundScore = 0;
		
		discardDecreeCard();
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
	 * Used to output to the screen the results.
	 */
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
		System.out.println(p1_VictoryPoints + "-" + p2_VictoryPoints);
		
		double averagep1_VictoryPoints = (double) p1_VictoryPoints / recordedRounds;
		double averagep2_VictoryPoints = (double) p2_VictoryPoints / recordedRounds;
		System.out.println("Average Victory Points per round");
		System.out.println(averagep1_VictoryPoints + "-" + averagep2_VictoryPoints);
		
		System.out.println("WON " + wonRounds + "/" + recordedRounds);
		System.out.println("WIN PERCENTAGE: " + (double) wonRounds / recordedRounds * 100);
		
		int winLow = 0; 
		for (int i = 0; i < 4; i++) {
			winLow += roundResults[i];
		}
		System.out.println("WON LOW: " + (double) winLow / recordedRounds * 100);
		
		int lostLow = 0; 
		for (int i = 4; i < 7; i++) {
			lostLow += roundResults[i];
		}
		System.out.println("LOST LOW: " + (double) lostLow / recordedRounds * 100);
		
		int winHigh = 0; 
		for (int i = 7; i < 10; i++) {
			winHigh += roundResults[i];
		}
		System.out.println("WON HIGH: " + (double) winHigh / recordedRounds * 100);
		
		int lostHigh = 0; 
		for (int i = 10; i < 14; i++) {
			lostHigh += roundResults[i];
		}
		System.out.println("LOST HIGH: " + (double) lostHigh / recordedRounds * 100);
		
		
		
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
			return p1_RoundScore;
		else
			return p2_RoundScore;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getOtherRoundScore(Player player) {
		if (player == p1)
			return p2_RoundScore;
		else
			return p1_RoundScore;
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
