
import java.util.Scanner;

import edu.truman.To.fox_forest.*;


public class TheFoxInTheForest {
	
	public static void main(String[] args) {
		System.out.println("Select an option:");
		System.out.println("1) Human vs Human (For testing only)");
		System.out.println("2) Human vs LowDummyNpc");
		System.out.println("3) Human vs HighDummyNpc");
		
		int userInput = -1;
		Scanner scandy = new Scanner(System.in);
		System.out.println("Select an option above, or any other number to exit.");
		userInput = scandy.nextInt();
		
		if (userInput == 1) {
			TwoHumanGame game = new TwoHumanGame();
			game.playGame();
		}
		else if (userInput == 2) {
			HumanVsNpcGame game = new HumanVsNpcGame(new LowDummyNpc());
			game.playGame();
		}
		else if (userInput == 3) {
			HumanVsNpcGame game = new HumanVsNpcGame(new HighDummyNpc());
			game.playGame();
		}
	}

}
