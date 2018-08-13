package edu.truman.To.fox_forest;

/**
 * Represents the entire game, including the players, cards, and scores.
 * Both Players are controlled by the computer.
 * 
 * @author ct2883
 *
 */

public class NpcVsNpcGame {

	static final int HAND_SIZE = 13;
	static final int WIN_SCORE = 21;
	static final int[] endOfRoundScore = {6,6,6,6,1,2,3,6,6,6,0,0,0,0};
	
	final Deck deck;
	final Npc npc;
	final Npc otherNpc;
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
	 * Creates a Game with two Npc players.
	 */
	public NpcVsNpcGame(Npc npc, Npc otherNpc) {
		deck = new Deck();
		this.npc = npc;
		this.otherNpc = otherNpc;
	}
	
	/**
	 * Plays a game of The Fox In The Forest.
	 */
	public void playGame() {
		int coinFlip = (int) (Math.random());
		npcFirst = coinFlip == 0? true : false;
		while (npcVictoryPoints < WIN_SCORE && otherNpcVictoryPoints < WIN_SCORE) {
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
		determineWinnerAndPrint();
	}
	
	/**
	 * Checks for and handles the Swan, Fox, and Woodcutter abilities.
	 * Used after a Npc plays a card.
	 * 
	 * @param npc the Npc who played the card.
	 * @param card the card that was played.
	 */
	private void handleFirstThreeAbilities(Npc npc, Card card) {
		if (card.getValue() == Card.SWAN_VALUE) {
			if (this.npc == npc) {
				isNpcSwan = true;
			}
			else {
				isOtherNpcSwan = true;
			}
		}
		else if (card.getValue() == Card.FOX_VALUE) {
			decreeCard = npc.swapDecreeCard(decreeCard);
		}
		else if (card.getValue() == Card.WOODCUTTER_VALUE) {
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
		if (npcCard.getValue() == 7)
			treasureBonus++;
		if (otherNpcCard.getValue() == 7)
			treasureBonus++;
		
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
		boolean isNpcWitch = npcCard.getValue() == Card.WITCH_VALUE ? true : false;
		boolean isOtherNpcWitch = otherNpcCard.getValue() == Card.WITCH_VALUE ? true : false;
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
		System.out.println("\n\nEND OF ROUND RESULTS: ");
		System.out.println(npcRoundScore + "-" + otherNpcRoundScore);
		System.out.println("P1 gains +" + endOfRoundScore[npcRoundScore]);
		System.out.println("P2 gains +" + endOfRoundScore[otherNpcRoundScore]);
		npcVictoryPoints += endOfRoundScore[npcRoundScore];
		otherNpcVictoryPoints += endOfRoundScore[otherNpcRoundScore];
		System.out.println("CURRENT VICTORY POINTS: ");
		System.out.println(npcVictoryPoints + "-" + otherNpcVictoryPoints);
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
	
	/**
	 * Determines the winner of the game and prints the results.
	 */
	private void determineWinnerAndPrint() {
		System.out.println("\n\nFINAL SCORE: " + npcVictoryPoints + "-" + otherNpcVictoryPoints);
		if (npcVictoryPoints > otherNpcVictoryPoints) {
			System.out.println("P1 WINS");
		}
		else if (npcVictoryPoints < otherNpcVictoryPoints) {
			System.out.println("P2 WINS");
		}
		else if (endOfRoundScore[npcRoundScore] > endOfRoundScore[otherNpcRoundScore]) {
			System.out.println("P1 WINS BY WINNING THE LAST ROUND");
		}
		else {
			System.out.println("P2 WINS BY WINNING THE LAST ROUND");
		}
	}
}
