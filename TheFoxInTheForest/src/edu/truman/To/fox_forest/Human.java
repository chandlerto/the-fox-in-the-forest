package edu.truman.To.fox_forest;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a Player controlled by the user.
 * 
 * @author Chandler To
 *
 */
public class Human implements Player {

	private ArrayList<Card> hand;
	
	/**
	 * Creates a Human object containing no cards.
	 */
	public Human() {
		hand = new ArrayList<Card>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawHand(Deck deck, int handSize) {
		while (hand.size() < handSize) {
			hand.add(deck.draw());
		}
		sortHand();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sortHand() {
		for (int i = 0; i < hand.size(); i++) {
			int minIdx= 0;
			for (int j = 1; j < hand.size()-i; j++) {
				if (hand.get(minIdx).compareTo(hand.get(j)) > 0) {
					minIdx = j;
				}
			}
			hand.add(hand.remove(minIdx));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card selectCardFirst() {
		this.printHand();
		System.out.println("Select any card to play.");
		
		Scanner scandy = new Scanner(System.in);
		int userInput = -1;
		boolean invalidInput = true;
		while (invalidInput) {
			userInput = scandy.nextInt();
			if (userInput > 0 && userInput <= hand.size()) {
				invalidInput = false;
			}
			else {
				System.out.println("Invalid choice, please pick again.");
			}
		}
		return hand.remove(userInput-1);
	}

	/**
	 * Prints to the console the player's hand in a numbered list.
	 */
	private void printHand() {
		int i = 1;
		for (Card card : hand) {
			System.out.println(i + ") " + card.toString());
			i++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card selectCardSecond(Card lead) {
		if (hasSuit(lead.getSuit())) {
			if (lead.getValue() == Card.MONARCH) {
				System.out.println("You must play either your highest " + Card.SUITS[lead.getSuit()] +
									" or the 1 of that suit.");
				int highIdx = getHighIdx(lead.getSuit());
				int oneIdx = hand.indexOf(new Card(1, lead.getSuit()));
				if (oneIdx != -1 && oneIdx != highIdx) {
					System.out.println(oneIdx+1 + ") " + hand.get(oneIdx).toString());
				}
				System.out.println(highIdx+1 + ") " + hand.get(highIdx).toString());
				
				Scanner scandy = new Scanner(System.in);
				int userInput = -1;
				boolean invalidInput = true;
				while (invalidInput) {
					userInput = scandy.nextInt();
					if (userInput > 0 && (userInput == oneIdx+1 || userInput == highIdx+1)) {
						invalidInput = false;
					}
					else {
						System.out.println("Invalid choice, please pick again.");
					}
				}
				return hand.remove(userInput-1);
			}
			
			else {
				this.printHand();
				System.out.println("You must select a " + Card.SUITS[lead.getSuit()] + " card.");
				
				Scanner scandy = new Scanner(System.in);
				int userInput = -1;
				boolean invalidInput = true;
				while (invalidInput) {
					userInput = scandy.nextInt();
					if (userInput > 0 && userInput <= hand.size() &&
						hand.get(userInput-1).getSuit() == lead.getSuit()) {
						invalidInput = false;
					}
					else {
						System.out.println("Invalid choice, please pick again.");
					}
				}
				return hand.remove(userInput-1);
			}
		}
		
		else {
			this.printHand();
			System.out.println("You may play any card, as you can't match suit.");
			
			Scanner scandy = new Scanner(System.in);
			int userInput = -1;
			boolean invalidInput = true;
			while (invalidInput) {
				userInput = scandy.nextInt();
				if (userInput > 0 && userInput <= hand.size()) {
					invalidInput = false;
				}
				else {
					System.out.println("Invalid choice, please pick again.");
				}
			}
			return hand.remove(userInput-1);
		}
	}
		
	/**
	 * Determines if the player contains a card of the given suit.
	 * 
	 * @param leadSuit the suit to match.
	 * @return true if the player has a card of the given suit, false otherwise.
	 */
	private boolean hasSuit(int leadSuit) {
		int lo = 0;
		int hi = hand.size();
		while (lo < hi) {
			int mid = (lo+hi) / 2;
			int curSuit = hand.get(mid).getSuit();
			if (curSuit == leadSuit) {
				return true;
			}
			else if (curSuit > leadSuit) {
				hi = mid;
			}
			else if (curSuit < leadSuit) {
				lo = mid+1;
			}
		}
		return false;
	}
	
	/**
	 * Returns the index of the highest value card of the given suit.
	 * If no such card is found, 0 is returned.
	 * 
	 * @param leadSuit the suit to match.
	 * @return the index of the highest value card of the given suit.
	 */
	private int getHighIdx(int leadSuit) {
		for (int i = hand.size()-1; i >= 0; i--) {
			if (hand.get(i).getSuit() == leadSuit) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card swapDecreeCard(Card current) {
		Scanner scandy = new Scanner(System.in);
		int userInput = 1;
		System.out.println("USING FOX ABILITY: SWAP WITH DECREE CARD");
		System.out.println("0) " + current.toString());
		this.printHand();
		System.out.println("Select any card to swap with the Decree Card (or enter 0 to not swap).");
		boolean invalidInput = true;
		while (invalidInput) {
			userInput = scandy.nextInt();
			if (userInput >= 0 && userInput <= hand.size()) {
				invalidInput = false;
			}
			else {
				System.out.println("Invalid choice, please pick again.");
			}
		}
		if (userInput == 0) {
			return current;
		}
		else {
			Card newDecree = hand.remove(userInput -1);
			hand.add(current);
			this.sortHand();
			return newDecree;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card drawAndDiscard(Card drawn) {
		Scanner scandy = new Scanner(System.in);
		int userInput = 1;
		System.out.println("USING WOODCUTTER ABILITY: DRAW AND DISCARD A CARD");
		System.out.println("0) " + drawn.toString());
		this.printHand();
		System.out.println("Select any card to discard (or enter 0 to discard the drawn card).");
		boolean invalidInput = true;
		while (invalidInput) {
			userInput = scandy.nextInt();
			if (userInput >= 0 && userInput <= hand.size()) {
				invalidInput = false;
			}
			else {
				System.out.println("Invalid choice, please pick again.");
			}
		}
		if (userInput == 0) {
			return drawn;
		}
		else {
			Card discard = hand.remove(userInput -1);
			hand.add(drawn);
			this.sortHand();
			return discard;
		}
	}
}
