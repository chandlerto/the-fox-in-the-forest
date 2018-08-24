
import java.util.Scanner;

import edu.truman.To.fox_forest.*;


public class TheFoxInTheForest {
	
	static Game game;
	static Npc npc;
	
	public static void main(String[] args) {
		System.out.println("Select an option:");
		System.out.println("-1) Npc Vs Npc for a select amount of rounds");
		System.out.println("0) NPC VS NPC");
		System.out.println("1) Human vs Human (For testing only)");
		System.out.println("2) Human vs LowDummyNpc");
		System.out.println("3) Human vs HighDummyNpc");
		System.out.println("4) Human vs RandomNpc");
		System.out.println("5) Human vs SimpleNpc");
				
		int userInput = -1;
		Scanner scandy = new Scanner(System.in);
		System.out.println("Select an option above, or any other number to exit.");
		userInput = scandy.nextInt();
		
		if (userInput == -1) {
			Npc p1;
			Npc p2;
			System.out.println("1) LowDummyNpc");
			System.out.println("2) HighDummyNpc");
			System.out.println("3) RandomNpc");
			System.out.println("4) SimpleNpc");
			System.out.println("Select the type of NPC for P1."); 
			userInput = scandy.nextInt();
			while (userInput < 1 && userInput > 3) {
				System.out.println("Invalid choice, try again.");
				userInput = scandy.nextInt();
			}
			if (userInput == 1) 
				p1 = new LowDummyNpc();
			else if (userInput == 2) 
				p1 = new HighDummyNpc();
			else if (userInput == 3)
				p1 = new RandomNpc();
			else 
				p1 = new SimpleNpc();
			
			System.out.println("1) LowDummyNpc");
			System.out.println("2) HighDummyNpc");
			System.out.println("3) RandomNpc");
			System.out.println("4) SimpleNpc");
			System.out.println("Select the type of NPC for P2."); 
			userInput = scandy.nextInt();
			while (userInput < 1 && userInput > 3) {
				System.out.println("Invalid choice, try again.");
				userInput = scandy.nextInt();
			}
			if (userInput == 1) 
				p2 = new LowDummyNpc();
			else if (userInput == 2) 
				p2 = new HighDummyNpc();
			else if (userInput == 3)
				p2 = new RandomNpc();
			else
				p2 = new SimpleNpc();
			
			System.out.println("Enter how many rounds will be played.");
			int numRounds = scandy.nextInt();
			
			NpcVsNpcRoundSet game = new NpcVsNpcRoundSet(p1, p2, numRounds);
			p1.addGame(game);
			p2.addGame(game);
			game.playRounds();
		}
		
		else if (userInput == 0) {
			Npc p1;
			Npc p2;
			System.out.println("1) LowDummyNpc");
			System.out.println("2) HighDummyNpc");
			System.out.println("3) RandomNpc");
			System.out.println("Select the type of NPC for P1."); 
			userInput = scandy.nextInt();
			while (userInput < 1 && userInput > 3) {
				System.out.println("Invalid choice, try again.");
				userInput = scandy.nextInt();
			}
			if (userInput == 1) 
				p1 = new LowDummyNpc();
			else if (userInput == 2) 
				p1 = new HighDummyNpc();
			else 
				p1 = new RandomNpc();
			
			System.out.println("1) LowDummyNpc");
			System.out.println("2) HighDummyNpc");
			System.out.println("3) RandomNpc");
			System.out.println("Select the type of NPC for P2."); 
			userInput = scandy.nextInt();
			while (userInput < 1 && userInput > 3) {
				System.out.println("Invalid choice, try again.");
				userInput = scandy.nextInt();
			}
			if (userInput == 1) 
				p2 = new LowDummyNpc();
			else if (userInput == 2) 
				p2 = new HighDummyNpc();
			else  
				p2 = new RandomNpc();
			
			NpcVsNpcGame game = new NpcVsNpcGame(p1, p2);
			p1.addGame(game);
			p2.addGame(game);
			game.playGame();
		}
		
		else if (userInput == 1) {
			TwoHumanGame game = new TwoHumanGame();
			game.playGame();
		}
		else if (userInput == 2) {
			npc = new LowDummyNpc();
			HumanVsNpcGame game = new HumanVsNpcGame(npc);
			npc.addGame(game);
			game.playGame();
		}
		else if (userInput == 3) {
			npc = new HighDummyNpc();
			HumanVsNpcGame game = new HumanVsNpcGame(npc);
			npc.addGame(game);
			game.playGame();
		}
		else if (userInput == 4) {
			npc = new RandomNpc();
			HumanVsNpcGame game = new HumanVsNpcGame(npc);
			npc.addGame(game);
			game.playGame();
		}
		else if (userInput == 5) {
			npc = new SimpleNpc();
			HumanVsNpcGame game = new HumanVsNpcGame(npc);
			npc.addGame(game);
			game.playGame();
		}
	}

}
